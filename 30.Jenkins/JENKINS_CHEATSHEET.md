# 🚀 Jenkins Cheatsheet for Backend Developers

**A practical, concise reference guide for Jenkins basics to advanced pipelines.**

---

## 1️⃣ Jenkins Basics

### What is Jenkins?

Jenkins is an **open-source automation server** that continuously builds, tests, and deploys your software. It runs jobs triggered by code changes, on a schedule, or manually.

### Key Concepts

| Concept | Definition |
|---------|-----------|
| **Job** | A single task/unit of work (build, test, deploy) |
| **Pipeline** | Orchestration of multiple jobs in sequence/parallel |
| **Node** | Physical/virtual machine where Jenkins executes jobs |
| **Executor** | Slot on a node where a job can run (parallelism control) |
| **Stage** | Logical grouping of steps in a pipeline |
| **Step** | Individual command or action (e.g., `sh 'npm test'`) |
| **Workspace** | Directory where job files are checked out & built |

---

## 2️⃣ Installation & Setup

### Install Jenkins (Linux)

```bash
# Install Java (prerequisite)
sudo apt update
sudo apt install openjdk-11-jdk -y

# Add Jenkins repository
wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo apt-key add -
sudo sh -c 'echo deb https://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'

# Install Jenkins
sudo apt update
sudo apt install jenkins -y
```

### Install Jenkins (Docker) ⭐ **Recommended**

```bash
# Pull & run Jenkins image
docker run -d --name jenkins \
  -p 8080:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  jenkins/jenkins:lts

# Get initial admin password
docker logs jenkins | grep -A 5 "Initial Admin password"
```

### Start & Access Jenkins

```bash
# Linux
sudo systemctl start jenkins
sudo systemctl status jenkins

# Docker
docker start jenkins
docker logs jenkins
```

**Access:** `http://localhost:8080`

**Initial Setup:**
1. Enter admin password (from logs)
2. Install suggested plugins
3. Create first admin user
4. Configure Jenkins URL (e.g., `http://your-server:8080`)

---

## 3️⃣ Common Jenkins Commands

### Linux Service Commands

```bash
# Start
sudo systemctl start jenkins

# Stop
sudo systemctl stop jenkins

# Restart
sudo systemctl restart jenkins

# Status
sudo systemctl status jenkins

# Enable auto-start on boot
sudo systemctl enable jenkins
```

### View Logs

```bash
# Linux
sudo tail -f /var/log/jenkins/jenkins.log

# Docker
docker logs -f jenkins

# In browser: Jenkins UI → System Log
```

### Jenkins CLI (Advanced)

```bash
# Download CLI jar
curl http://localhost:8080/jnlpJars/jenkins-cli.jar -o jenkins-cli.jar

# Trigger a job
java -jar jenkins-cli.jar -s http://localhost:8080 build "JobName"

# List jobs
java -jar jenkins-cli.jar -s http://localhost:8080 list-jobs
```

---

## 4️⃣ Jenkins Pipeline - Core Concepts

### Declarative vs Scripted Pipeline

| Feature | **Declarative** | **Scripted** |
|---------|-----------------|------------|
| **Syntax** | Simple, structured YAML-like | Groovy code, flexible |
| **Learning Curve** | Easy → Intermediate | Intermediate → Advanced |
| **Use Case** | Most projects ✅ | Complex, custom logic |
| **Readability** | High | Medium |
| **Example** | `stages { stage('Build') { steps { ... } } }` | `node { stage('Build') { ... } }` |

### Basic Declarative Pipeline Structure

```groovy
pipeline {
    agent any                          // Run on any available executor
    
    environment {                      // Set environment variables
        JAVA_HOME = '/usr/lib/jvm/java-11'
    }
    
    stages {                           // Define execution stages
        stage('Checkout') {
            steps {
                checkout scm           // Clone from Git
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean install' // Execute shell command
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        
        stage('Deploy') {
            when {
                branch 'main'          // Only on main branch
            }
            steps {
                sh './deploy.sh'
            }
        }
    }
    
    post {                             // Run after stages (always/success/failure)
        always {
            junit '**/target/surefire-reports/*.xml'  // Archive test results
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
```

### Pipeline Components Explained

```groovy
agent any                              // Execution node
    // Options: any, none, label "label-name", docker "image:tag"

stages                                 // Ordered execution units
    stage('Name') {                    // Individual stage
        steps {                        // Commands to run
            sh 'echo Hello'            // Shell command
            bat 'echo Hello'           // Windows command
        }
    }

when {                                 // Conditional execution
    branch 'main'
    environment name: 'ENV', value: 'prod'
    changeRequest()                    // PR trigger
}

post {                                 // Cleanup/notifications
    always { }
    success { }
    failure { }
    unstable { }
    cleanup { }
}
```

---

## 5️⃣ Jenkinsfile Examples

### ✅ Simple Build Pipeline

```groovy
pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                echo '🔨 Building...'
                sh 'mvn clean package'
            }
        }
    }
    
    post {
        success {
            echo '✅ Build successful!'
        }
        failure {
            echo '❌ Build failed!'
        }
    }
}
```

### ✅ CI Pipeline (Build + Test + Quality)

```groovy
pipeline {
    agent any
    
    triggers {
        githubPush()                   // Trigger on Git push
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }
        
        stage('Code Quality') {
            steps {
                sh 'mvn sonar:sonar'    // SonarQube analysis
            }
        }
        
        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar'
            }
        }
    }
    
    post {
        always {
            junit 'target/surefire-reports/*.xml'
            publishHTML([
                reportDir: 'target/site/jacoco',
                reportFiles: 'index.html',
                reportName: 'Code Coverage'
            ])
        }
    }
}
```

### ✅ CD Pipeline (Build + Test + Deploy)

```groovy
pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = 'docker.io'
        DOCKER_IMAGE = "${DOCKER_REGISTRY}/myapp:${BUILD_NUMBER}"
        DEPLOY_SERVER = 'prod.example.com'
    }
    
    stages {
        stage('Build & Test') {
            steps {
                sh 'mvn clean package'
                sh 'mvn test'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE} .'
            }
        }
        
        stage('Push to Registry') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${DOCKER_IMAGE}
                    '''
                }
            }
        }
        
        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            steps {
                sshagent(['prod-ssh-key']) {
                    sh '''
                        ssh -o StrictHostKeyChecking=no deploy@${DEPLOY_SERVER} \
                        "docker pull ${DOCKER_IMAGE} && docker run -d ${DOCKER_IMAGE}"
                    '''
                }
            }
        }
    }
    
    post {
        failure {
            emailext(
                subject: "Build Failed: ${JOB_NAME} #${BUILD_NUMBER}",
                body: "Check console at: ${BUILD_URL}",
                to: "team@example.com"
            )
        }
    }
}
```

---

## 6️⃣ Environment Variables

### Built-in Jenkins Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `${BUILD_NUMBER}` | Build number | `42` |
| `${BUILD_ID}` | Build ID | `2024-04-27_10-30-45` |
| `${JOB_NAME}` | Job name | `my-app-build` |
| `${WORKSPACE}` | Job workspace path | `/var/jenkins_home/workspace/my-app` |
| `${GIT_COMMIT}` | Current Git commit hash | `abc123def456...` |
| `${GIT_BRANCH}` | Current Git branch | `origin/main` |
| `${BUILD_URL}` | Jenkins build URL | `http://jenkins:8080/job/my-app/42/` |
| `${BRANCH_NAME}` | Branch name (Pipeline) | `main`, `develop` |

### Custom Environment Variables

```groovy
pipeline {
    agent any
    
    environment {
        // String
        APP_NAME = 'my-app'
        VERSION = '1.0.0'
        
        // From credentials
        DATABASE_URL = credentials('db-url')
        
        // From shell command
        BUILD_DATE = sh(script: "date '+%Y-%m-%d'", returnStdout: true).trim()
    }
    
    stages {
        stage('Print Env') {
            steps {
                sh 'echo "Building ${APP_NAME} version ${VERSION}"'
                sh 'echo "Build Date: ${BUILD_DATE}"'
                sh 'printenv | sort'  // Print all env vars
            }
        }
    }
}
```

### Access in Steps

```groovy
steps {
    sh 'echo ${GIT_BRANCH}'              // In shell script
    script {
        def branch = env.GIT_BRANCH      // In Groovy code
        echo "Current branch: ${branch}"
    }
}
```

---

## 7️⃣ Essential Plugins

### Must-Have Plugins

| Plugin | Purpose | Install |
|--------|---------|---------|
| **Pipeline** | Pipeline support | Usually pre-installed |
| **GitHub/GitLab Integration** | Git webhook & integration | Manage Jenkins → Plugins |
| **Docker Pipeline** | Docker commands in pipeline | `Docker Pipeline` plugin |
| **Credentials Binding** | Securely use credentials | `Credentials Binding Plugin` |
| **Email Extension** | Email notifications | `Email Extension Plugin` |
| **JUnit** | Test result parsing | Usually pre-installed |
| **Timestamper** | Add timestamps to logs | `Log Parser Plugin` |
| **Slack Notification** | Slack alerts | `Slack Notification Plugin` |
| **SonarQube Scanner** | Code quality analysis | `SonarQube Scanner` plugin |
| **Blue Ocean** | Modern UI (optional) | `Blue Ocean` plugin |

### Install a Plugin

**Via UI:**
1. Manage Jenkins → Plugin Manager
2. Search plugin name
3. Click "Install without restart"
4. Check "Restart Jenkins when installation complete"

**Via Script:**
```groovy
import jenkins.model.Jenkins
import hudson.PluginWrapper

Jenkins.instance.pluginManager.plugins.each { plugin ->
    println "${plugin.getShortName()}:${plugin.getVersion()}"
}
```

---

## 8️⃣ Git Integration

### Connect Jenkins with GitHub

#### Step 1: Generate GitHub Token

1. GitHub → Settings → Developer Settings → Personal Access Tokens
2. Generate new token
3. Scopes: `repo`, `admin:repo_hook`
4. Copy token

#### Step 2: Add Credentials in Jenkins

1. Jenkins → Manage Credentials → System → Global Credentials
2. Add Credentials → Username with password
3. Username: `github-user`
4. Password: `<paste token>`
5. ID: `github-credentials`

#### Step 3: Configure Job

```groovy
pipeline {
    agent any
    
    triggers {
        githubPush()  // Trigger on GitHub push
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/user/repo.git',
                        credentialsId: 'github-credentials'
                    ]]
                ])
            }
        }
    }
}
```

### GitHub Webhook Setup

1. Repository → Settings → Webhooks → Add webhook
2. **Payload URL:** `http://your-jenkins:8080/github-webhook/`
3. **Content type:** `application/json`
4. **Events:** Push events
5. **Active:** ✅

---

## 9️⃣ Credentials Management

### Store Credentials Securely

#### Option 1: Jenkins UI

1. Manage Jenkins → Manage Credentials
2. System → Global Credentials → Add Credentials
3. Choose type:
   - **Username with password** (Git, Docker Hub)
   - **SSH Key** (Servers, Git)
   - **Secret text** (Tokens, API keys)
   - **Certificate** (HTTPS)

#### Option 2: Store in Pipeline

```groovy
pipeline {
    agent any
    
    stages {
        stage('Use Credentials') {
            steps {
                // Method 1: Username + Password
                withCredentials([usernamePassword(
                    credentialsId: 'docker-hub',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh 'docker login -u $DOCKER_USER -p $DOCKER_PASS'
                }
                
                // Method 2: Secret Text
                withCredentials([string(
                    credentialsId: 'slack-token',
                    variable: 'SLACK_TOKEN'
                )]) {
                    sh 'curl -X POST -d "token=$SLACK_TOKEN" ...'
                }
                
                // Method 3: SSH Key
                withCredentials([sshUserPrivateKey(
                    credentialsId: 'deploy-key',
                    keyFileVariable: 'SSH_KEY'
                )]) {
                    sh 'scp -i $SSH_KEY app.jar deploy@server:/tmp/'
                }
            }
        }
    }
}
```

### ⚠️ Never Hardcode Secrets

❌ **Bad:**
```groovy
sh 'docker login -u myuser -p mypassword'
```

✅ **Good:**
```groovy
withCredentials([usernamePassword(credentialsId: 'docker-creds', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
    sh 'docker login -u $USER -p $PASS'
}
```

---

## 🔟 Docker + Jenkins

### Run Jenkins in Docker with Docker-in-Docker (DinD)

```bash
docker run -d \
  --name jenkins \
  -p 8080:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v $(which docker):/usr/bin/docker \
  --group-add $(stat -c %g /var/run/docker.sock) \
  jenkins/jenkins:lts
```

### Use Docker in Pipeline

```groovy
pipeline {
    agent any
    
    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://docker.io', 'docker-credentials') {
                        def app = docker.build("myapp:${BUILD_NUMBER}")
                        app.push()
                        app.push('latest')
                    }
                }
            }
        }
        
        stage('Run Container') {
            steps {
                sh '''
                    docker run --rm \
                    -e JAVA_OPTS="-Xmx512m" \
                    myapp:${BUILD_NUMBER} \
                    java -jar app.jar
                '''
            }
        }
        
        stage('Push to Registry') {
            steps {
                script {
                    docker.image("myapp:${BUILD_NUMBER}").push('latest')
                }
            }
        }
    }
}
```

### Multi-Stage Docker Build in Jenkins

```groovy
stage('Build') {
    steps {
        script {
            docker.build("myapp:${BUILD_NUMBER}", \
                "--build-arg JAVA_VERSION=11 .")
        }
    }
}
```

---

## 1️⃣1️⃣ Best Practices

### ✅ Pipeline Structuring

```groovy
// ✅ Good structure
pipeline {
    agent any
    
    environment {
        // Centralize configs
        APP_NAME = 'myapp'
        VERSION = '1.0.0'
    }
    
    stages {
        stage('Prepare') {
            steps {
                script {
                    // Prepare logic
                }
            }
        }
        
        stage('Build') {
            steps {
                script {
                    // Build logic
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    // Test logic
                }
            }
        }
        
        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                script {
                    // Deploy logic
                }
            }
        }
    }
    
    post {
        always {
            // Cleanup resources
            deleteDir()
        }
        
        success {
            // Success notifications
        }
        
        failure {
            // Failure handling
        }
    }
}
```

### ✅ Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| **Job Name** | `kebab-case` | `user-service-build` |
| **Branch Names** | `kebab-case` | `feature/auth-module` |
| **Variables** | `UPPER_SNAKE_CASE` | `DOCKER_REGISTRY` |
| **Functions** | `camelCase` | `buildDocker()` |
| **Stage Names** | `Title Case` | `Build & Test` |

### ✅ Security Best Practices

```groovy
pipeline {
    agent any
    
    // ✅ Use credentials plugin
    environment {
        DB_PASSWORD = credentials('db-password')
        API_TOKEN = credentials('api-token')
    }
    
    options {
        // ✅ Timeout pipelines
        timeout(time: 1, unit: 'HOURS')
        
        // ✅ Keep build history
        buildDiscarder(logRotator(numToKeepStr: '10'))
        
        // ✅ Disable concurrent builds
        disableConcurrentBuilds()
    }
    
    stages {
        stage('Security Check') {
            steps {
                // ✅ Scan dependencies
                sh 'mvn org.owasp:dependency-check-maven:check'
                
                // ✅ SAST analysis
                sh 'mvn sonar:sonar'
            }
        }
    }
}
```

### ✅ Other Tips

- **Use `script` block** for complex Groovy logic
- **Keep stages focused** (one responsibility per stage)
- **Parallel stages** for faster builds:
  ```groovy
  parallel {
      stage('Unit Tests') { steps { sh 'mvn test' } }
      stage('Code Quality') { steps { sh 'mvn sonar:sonar' } }
  }
  ```
- **Reuse common logic** with shared libraries
- **Document complex pipelines** with comments
- **Test Jenkinsfile locally** with `declarative linter`

---

## 1️⃣2️⃣ Common Errors & Fixes

| Error | Cause | Fix |
|-------|-------|-----|
| **Port 8080 already in use** | Another process on port 8080 | `lsof -i :8080` / Kill process or use `docker run -p 8081:8080` |
| **Permission denied on `/var/run/docker.sock`** | Jenkins user can't access Docker | Add Jenkins to docker group: `sudo usermod -aG docker jenkins` |
| **Git credentials not working** | Invalid token/SSH key | Regenerate token, verify credentials in Jenkins UI |
| **Pipeline syntax error** | Invalid Groovy/YAML | Use Replay feature to test changes |
| **Agent unavailable** | No executors free | Increase executors or add more nodes |
| **Build timeout** | Slow build | Increase timeout: `timeout(time: 2, unit: 'HOURS')` |
| **Docker image not found** | Image not built/pulled | Check docker image exists: `docker images` |
| **Artifact not found** | Wrong path in archiveArtifacts | Check workspace path: `${WORKSPACE}/target/*.jar` |
| **Webhook not triggering** | GitHub webhook misconfigured | Verify payload URL & GitHub credentials |
| **Email not sending** | SMTP not configured | Configure in Manage Jenkins → Email Configuration |

### Debugging Tips

```bash
# Check Jenkins logs
docker logs -f jenkins

# SSH to Jenkins container
docker exec -it jenkins bash

# Verify Docker is accessible
docker ps

# Check pipeline syntax
curl -X POST -F "jenkinsfile=<Jenkinsfile" http://localhost:8080/pipeline-model-converter/validate

# Run in debug mode
export JENKINS_NODE_COOKIE=dontKillMe
```

---

## 1️⃣3️⃣ Quick Commands Reference

```bash
# ===== JENKINS SERVICE =====
sudo systemctl start jenkins
sudo systemctl stop jenkins
sudo systemctl restart jenkins
sudo systemctl status jenkins
sudo systemctl enable jenkins

# ===== DOCKER =====
docker run -d -p 8080:8080 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts
docker logs -f jenkins
docker exec -it jenkins bash
docker stop jenkins
docker start jenkins

# ===== GIT =====
git clone https://github.com/user/repo.git
git checkout -b feature/branch-name
git add . && git commit -m "feat: description"
git push origin feature/branch-name

# ===== MAVEN =====
mvn clean package
mvn test
mvn sonar:sonar
mvn clean install -DskipTests

# ===== DOCKER BUILD & PUSH =====
docker build -t myapp:latest .
docker login
docker push myapp:latest
docker pull myapp:latest

# ===== USEFUL =====
# Check port in use
lsof -i :8080
netstat -tulpn | grep :8080

# Kill process on port 8080
kill -9 $(lsof -t -i:8080)

# View file logs
tail -f /var/log/jenkins/jenkins.log
```

---

## 🚀 Day 1 Jenkins Setup Flow

### Quick Start (30 minutes)

```
1. ✅ Install Jenkins
   └─ Docker: docker run -d -p 8080:8080 jenkins/jenkins:lts
   
2. ✅ Access Jenkins
   └─ Open http://localhost:8080
   └─ Enter admin password (from logs)
   
3. ✅ Install plugins
   └─ Dashboard → Manage Jenkins → Plugin Manager
   └─ Install: Pipeline, GitHub Integration, Docker Pipeline
   
4. ✅ Add Git credentials
   └─ Manage Jenkins → Manage Credentials
   └─ Add GitHub token as Username+Password
   
5. ✅ Create first job
   └─ Dashboard → New Item → Pipeline
   └─ Name: my-first-pipeline
   └─ Pipeline → Definition: Pipeline script from SCM
   └─ SCM: Git
   └─ Repository URL: https://github.com/your/repo.git
   
6. ✅ Create Jenkinsfile in repo root:
   
   pipeline {
       agent any
       stages {
           stage('Build') {
               steps {
                   echo '🔨 Building...'
                   sh 'mvn clean package'
               }
           }
       }
   }

7. ✅ Trigger build
   └─ Click "Build Now"
   └─ View logs in real-time
   
8. ✅ Setup GitHub webhook
   └─ GitHub Repo → Settings → Webhooks
   └─ URL: http://your-jenkins:8080/github-webhook/
   └─ Push code → Jenkins auto-triggers! 🎉
```

---

## 💡 Interview Tips & Common Questions

### Q1: What's the difference between Jenkins and GitLab CI?

**Answer:**
- **Jenkins:** Self-hosted, highly flexible, requires setup
- **GitLab CI:** Integrated into GitLab, easier setup, cloud-native
- **Jenkins wins:** Complex workflows, multi-team setups
- **GitLab CI wins:** Speed, simplicity, less maintenance

### Q2: How do you handle secrets in Jenkins?

**Answer:**
"Use Jenkins **Credentials Manager** to store secrets securely. Access them in pipelines using `withCredentials()` block:
```groovy
withCredentials([string(credentialsId: 'api-key', variable: 'KEY')]) {
    sh 'curl -H "Authorization: Bearer $KEY" ...'
}
```
Never hardcode secrets in Jenkinsfile."

### Q3: Declarative or Scripted pipeline - which to use?

**Answer:**
"**Declarative** for most projects (simpler, cleaner, recommended). Use **Scripted** only when you need complex logic beyond declarative's capabilities (conditionals, loops, functions)."

### Q4: How do you parallelize builds?

**Answer:**
```groovy
parallel {
    stage('Unit Tests') { steps { sh 'mvn test' } }
    stage('Integration Tests') { steps { sh 'mvn verify' } }
    stage('Code Quality') { steps { sh 'mvn sonar:sonar' } }
}
```

### Q5: How do you debug a failing pipeline?

**Answer:**
"Three ways:
1. **Replay feature** - Rerun stage without git checkout
2. **Log analysis** - Check console output line-by-line
3. **Debug steps** - Add `sh 'printenv | sort'` and `sh 'ls -la'` to see state
4. **Interactive mode** - SSH into agent and debug manually"

### Q6: What are executors?

**Answer:**
"Executors are **parallel slots** on a Jenkins node. If a node has 2 executors, it can run 2 jobs simultaneously. Configure in: Manage Jenkins → Manage Nodes → Configure → Executors"

### Q7: How do you rollback a deployment?

**Answer:**
"Jenkins doesn't auto-rollback. You must:
1. Keep previous versions tagged
2. Trigger manual job with previous version
3. Or use deployment tools (Kubernetes, Ansible) with versioned releases
```groovy
stage('Rollback') {
    when {
        manual: true
    }
    steps {
        sh 'deploy.sh --version=1.0.1'
    }
}
```"

### Q8: What's a shared library?

**Answer:**
"Reusable Groovy code across pipelines. Located in `vars/` directory. Example:
```groovy
// vars/notify.groovy
def call(String status) {
    echo "Build ${status}"
}

// Use in pipeline
@Library('shared-library') _
notify('SUCCESS')
```"

---

## 📚 Additional Resources

- **Official Docs:** https://www.jenkins.io/doc/
- **Pipeline Syntax:** https://www.jenkins.io/doc/book/pipeline/syntax/
- **Declarative Linter:** http://localhost:8080/declarative-linter/
- **Blue Ocean (Modern UI):** https://plugins.jenkins.io/blueocean/
- **Jenkins Best Practices:** https://www.jenkins.io/solutions/

---

**Last Updated:** April 2026 | **Target Audience:** Backend/Full Stack Developers (Beginner → Intermediate)

**Quick Tip:** Bookmark this cheatsheet! 🔖 Refer back when stuck.
