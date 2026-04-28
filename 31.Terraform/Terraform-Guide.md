# Terraform Guide & Cheatsheet

A practical Terraform reference for developers using VS Code, Linux CLI, and AWS. This guide is focused on real usage, commands, and copy-paste ready examples.

---

## 1. Terraform Basics

- **Terraform** is a declarative Infrastructure as Code (IaC) tool. You describe the desired infrastructure state, and Terraform provisions it.
- **IaC concept**: treat infrastructure the same way as software code:
  - version control
  - review and pull requests
  - repeatable provisioning
  - automation
- **Workflow**:
  1. `terraform init` — initialize backend/plugins
  2. `terraform plan` — preview changes
  3. `terraform apply` — execute changes
  4. `terraform destroy` — remove resources

> Note: Terraform uses a state file (`terraform.tfstate`) to track deployed infrastructure.

---

## 2. Setup in IDE (VS Code)

### Required tools

- **Terraform CLI**
  - Linux: install from HashiCorp package repository or download the binary.
  - Windows: install via Chocolatey, Scoop, or direct download.
- **VS Code extensions**
  - `HashiCorp Terraform` — official extension for syntax, formatting, and validation.
  - `Terraform` by Mikael Olenfalk — alternative extension with highlighting.
  - Optional: `YAML` and `GitLens` for related config and version control.

### Installing Terraform CLI

#### Linux (Ubuntu/Debian)

```bash
sudo apt-get update
sudo apt-get install -y gnupg software-properties-common curl
curl -fsSL https://apt.releases.hashicorp.com/gpg | sudo gpg --dearmor -o /usr/share/keyrings/hashicorp-archive-keyring.gpg
echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] https://apt.releases.hashicorp.com $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/hashicorp.list
sudo apt-get update
sudo apt-get install -y terraform
```

#### Linux (Amazon Linux 2)

```bash
sudo yum install -y yum-utils
sudo yum-config-manager --add-repo https://rpm.releases.hashicorp.com/AmazonLinux/hashicorp.repo
sudo yum -y install terraform
```

#### Windows (Chocolatey)

```powershell
choco install terraform -y
```

#### Manual binary install

```bash
curl -O https://releases.hashicorp.com/terraform/1.6.7/terraform_1.6.7_linux_amd64.zip
unzip terraform_1.6.7_linux_amd64.zip
sudo mv terraform /usr/local/bin/
terraform version
```

### Recommended file layout

Keep Terraform project folders small and consistent.

```text
31.Terraform/01-app/
  ├─ main.tf
  ├─ provider.tf
  ├─ variables.tf
  ├─ outputs.tf
  ├─ terraform.tfvars
  ├─ backend.tf        # optional remote state backend
  ├─ locals.tf         # optional common values
  ├─ modules/          # optional reusable modules
  └─ README.md
```

### Why separate files?

- `provider.tf` keeps cloud config separate.
- `variables.tf` documents inputs.
- `outputs.tf` exposes useful values.
- `terraform.tfvars` stores environment-specific data.
- `backend.tf` configures state storage.

### How to run Terraform inside VS Code terminal

1. Open the integrated terminal: `Ctrl+``
2. Change to the Terraform directory:

```bash
cd "d:\Full Stack\Practice\Java, Spring, and Microservices\31.Terraform\01-app"
```

3. Run commands:

```bash
terraform init
terraform fmt -recursive
terraform validate
terraform plan -out=tfplan
terraform apply tfplan
```

> Tip: Use the built-in terminal in VS Code to keep path/context consistent with the workspace.

---

## 3. Setup on Linux / AWS EC2

### Install Terraform on Linux

Use the package manager for your distro, or install the official binary.

#### Ubuntu / Debian

```bash
sudo apt-get update
sudo apt-get install -y gnupg software-properties-common curl unzip
curl -fsSL https://apt.releases.hashicorp.com/gpg | sudo gpg --dearmor -o /usr/share/keyrings/hashicorp-archive-keyring.gpg
echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] https://apt.releases.hashicorp.com $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/hashicorp.list
sudo apt-get update
sudo apt-get install -y terraform
terraform version
```

#### Amazon Linux 2

```bash
sudo yum install -y yum-utils
sudo yum-config-manager --add-repo https://rpm.releases.hashicorp.com/AmazonLinux/hashicorp.repo
sudo yum -y install terraform
terraform version
```

#### Fedora / CentOS

```bash
sudo dnf config-manager --add-repo https://rpm.releases.hashicorp.com/fedora/hashicorp.repo
sudo dnf install -y terraform
```

#### If `terraform` is not found

```bash
which terraform
terraform version
```

If missing, add `/usr/local/bin` to `PATH` or move the binary there.

### Install AWS CLI

#### Linux

```bash
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
aws --version
```

#### Windows

```powershell
msiexec.exe /i https://awscli.amazonaws.com/AWSCLIV2.msi
aws --version
```

### Configure AWS credentials

#### Interactive setup

```bash
aws configure
```

Enter:
- AWS Access Key ID
- AWS Secret Access Key
- Default region name: `ap-south-1`
- Default output format: `json`

#### Environment variables

```bash
export AWS_ACCESS_KEY_ID="AKIA..."
export AWS_SECRET_ACCESS_KEY="abcd..."
export AWS_DEFAULT_REGION="ap-south-1"
```

#### AWS profile example

```bash
aws configure --profile terraform-dev
```

Then use it in Terraform:

```hcl
provider "aws" {
  region  = var.aws_region
  profile = "terraform-dev"
}
```

### IAM best practice

- Create an IAM user or role with least privilege.
- Grant permissions only for resources you manage.
- Avoid using root account keys.
- For EC2, use instance profiles or temporary credentials.

### Common CLI mistakes

- ⚠️ Running `terraform` with `sudo` changes ownership of `.terraform` and state files.
- ⚠️ Using the wrong AWS region for AMI, key pair, or resources.
- ⚠️ AWS CLI configured in one shell but Terraform run in another shell.
- ⚠️ `terraform init` must run after changing provider configuration.
- ⚠️ Not installing `unzip` when using the AWS CLI install bundle.

---

## 4. Core Terraform Concepts

### Providers

- Terraform providers are plugins that manage resources for a service.
- Example AWS provider block:

```hcl
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}
```

### Resources

- A resource represents a single cloud object.
- Terraform resources are defined using `resource "<type>" "<name>" { ... }`.
- Terraform tracks lifecycle and diff changes.

### Variables

- Use `variable` blocks to define reusable inputs.
- Values can come from:
  - `terraform.tfvars`
  - `*.auto.tfvars`
  - CLI flags `-var` / `-var-file`
  - environment variables `TF_VAR_<name>`

Example:

```hcl
variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t3.micro"
}
```

### Outputs

- Outputs surface values after apply.
- Can be used in automation or by other modules.

Example:

```hcl
output "instance_public_ip" {
  description = "Public IP address"
  value       = aws_instance.web.public_ip
}
```

### State file

- Terraform state is stored in `terraform.tfstate`.
- It maps your config to real resources and stores metadata.
- Never edit state manually unless you know exactly what you're doing.
- `.terraform.lock.hcl` locks provider versions.

### Dependency handling

- Terraform detects dependencies from references.
- Example:

```hcl
resource "aws_instance" "app" {
  subnet_id = aws_subnet.main.id
}
```

- Use `depends_on` only for explicit ordering when Terraform can't infer it.

Example:

```hcl
depends_on = [aws_iam_role.example]
```

### Other useful concepts

- **Data sources** read existing resources, e.g. `data "aws_ami"`.
- **Locals** store computed values.
- **Count** and `for_each` create multiple resources.
- **Provisioners** run scripts during creation (use sparingly).

---

## 5. Terraform Commands (VERY IMPORTANT)

| Command | Description | Example |
|---|---|---|
| `terraform init` | Initialize plugins, provider configs, and backend | `terraform init` |
| `terraform validate` | Validate configuration syntax and semantics | `terraform validate` |
| `terraform fmt` | Format files to canonical style | `terraform fmt -recursive` |
| `terraform plan` | Compute planned changes without applying them | `terraform plan -out=tfplan` |
| `terraform apply` | Apply changes to reach desired state | `terraform apply tfplan` |
| `terraform destroy` | Destroy managed resources | `terraform destroy` |
| `terraform show` | Show plan or state output | `terraform show tfplan` |
| `terraform state list` | List resources in current state | `terraform state list` |
| `terraform state show` | Show resource details from state | `terraform state show aws_instance.web` |
| `terraform state rm` | Remove resource from state tracking | `terraform state rm aws_instance.old` |
| `terraform graph` | Generate resource dependency graph | `terraform graph | dot -Tsvg > graph.svg` |
| `terraform workspace new` | Create a workspace for environment isolation | `terraform workspace new dev` |
| `terraform workspace select` | Switch workspaces | `terraform workspace select prod` |
| `terraform import` | Import existing resource into state | `terraform import aws_s3_bucket.app_bucket my-bucket` |

### Command workflow

```bash
# initialize the project
terraform init

# format and validate
terraform fmt -recursive
terraform validate

# preview planned changes
terraform plan -out=tfplan

# apply changes
terraform apply tfplan

# inspect state or plan
terraform show tfplan

# remove infrastructure
terraform destroy
```

### Working with variables from CLI

```bash
terraform plan -var="instance_type=t3.small" -var-file=terraform.tfvars
terraform apply -var="bucket_name=my-bucket-12345"
```

### Using workspaces

```bash
terraform workspace new dev
terraform workspace select dev
terraform workspace list
```

---

## 6. Writing Terraform Code (Examples)

### a) EC2 Instance example

`provider.tf`

```hcl
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}
```

`main.tf`

```hcl
resource "aws_instance" "linux_vm" {
  ami                    = var.ami_id
  instance_type          = var.instance_type
  key_name               = var.key_name
  associate_public_ip_address = true

  tags = {
    Name        = "AMAN_Terraform_Linux_VM"
    Environment = var.environment
  }
}
```

`variables.tf`

```hcl
variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "ap-south-1"
}

variable "ami_id" {
  description = "AMI ID for EC2"
  type        = string
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t3.micro"
}

variable "key_name" {
  description = "SSH key pair name"
  type        = string
}

variable "environment" {
  description = "Deployment environment"
  type        = string
  default     = "dev"
}
```

`terraform.tfvars`

```hcl
aws_region    = "ap-south-1"
ami_id        = "ami-0e12ffc2dd465f6e4"
instance_type = "t3.micro"
key_name      = "TerraformKp"
environment   = "dev"
```

### b) S3 Bucket example

`main.tf`

```hcl
resource "aws_s3_bucket" "app_bucket" {
  bucket = var.bucket_name
  acl    = "private"

  versioning {
    enabled = var.enable_versioning
  }

  server_side_encryption_configuration {
    rule {
      apply_server_side_encryption_by_default {
        sse_algorithm = "AES256"
      }
    }
  }

  tags = {
    Name        = "terraform-demo-bucket"
    Environment = var.environment
  }
}
```

`variables.tf`

```hcl
variable "bucket_name" {
  description = "S3 bucket name"
  type        = string
}

variable "enable_versioning" {
  description = "Enable S3 versioning"
  type        = bool
  default     = true
}
```

`terraform.tfvars`

```hcl
bucket_name        = "my-terraform-demo-bucket-12345"
enable_versioning = true
```

### c) Variables usage patterns

- `variables.tf` defines inputs.
- `terraform.tfvars` sets defaults for the current environment.
- Use `*.auto.tfvars` for automatic loading.
- Use `TF_VAR_<name>` environment variables for automation.

Example of CLI override:

```bash
terraform plan -var="bucket_name=my-app-bucket" -var="enable_versioning=false"
```

### d) Outputs example

`outputs.tf`

```hcl
output "instance_id" {
  description = "EC2 instance ID"
  value       = aws_instance.linux_vm.id
}

output "instance_public_ip" {
  description = "EC2 public IP"
  value       = aws_instance.linux_vm.public_ip
}

output "bucket_name" {
  description = "S3 bucket name"
  value       = aws_s3_bucket.app_bucket.bucket
}
```

### e) Data source example

```hcl
data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-*-x86_64-gp2"]
  }
}

resource "aws_instance" "linux_vm" {
  ami           = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type
}
```

---

## 7. Best Practices (IMPORTANT)

- Do not hardcode secrets, access keys, passwords, or tokens in `.tf` files.
- Use `variables.tf` and `terraform.tfvars` for environment-specific values.
- Keep sensitive input values out of source control using `sensitive = true` and environment variables.
- Use modules for reusable infrastructure and separate concerns.
- Use consistent naming conventions, e.g. `env-app-resource`.
- Use a remote backend for state storage and locking:
  - AWS: S3 backend + DynamoDB lock table
  - Terraform Cloud / Enterprise
  - Azure Storage account + blob lock
- Protect state files with encryption and access controls.
- Keep dependency declarations up to date with `required_providers`.
- Validate and format consistently:

```bash
terraform fmt -recursive
terraform validate
```

- Use workspaces or separate folders for `dev`, `staging`, and `prod`.
- Document inputs and outputs in `README.md`.
- Review `terraform plan` carefully before applying.
- Use `lifecycle` blocks sparingly for create_before_destroy or prevent_destroy.

### Recommended naming

- Resource naming: `project-environment-purpose`
- Bucket names: globally unique, lowercase, no spaces
- Instance names: include environment and role
- Tag values: `Environment`, `Owner`, `Project`, `CostCenter`

### Remote backend example

`backend.tf`

```hcl
terraform {
  backend "s3" {
    bucket         = "my-terraform-state-bucket"
    key            = "31-terraform/01-app/terraform.tfstate"
    region         = "ap-south-1"
    dynamodb_table = "terraform-lock-table"
    encrypt        = true
  }
}
```

---

## 8. Debugging & Common Errors

### No credentials found

```text
Error: No valid credential sources found for AWS Provider.
```

Fix:
- Run `aws configure`.
- Check `~/.aws/credentials` and `~/.aws/config`.
- Verify environment variables are set.
- Confirm `provider "aws"` region and profile match.

### Invalid AMI

```text
InvalidAMIID.NotFound: The image id '[ami-...]' does not exist
```

Fix:
- Use a region-specific AMI.
- Confirm the AMI is available in the same AWS region.
- Use a data source to fetch the latest AMI.

### Key pair not found

```text
InvalidKeyPair.NotFound: The key pair 'TerraformKp' does not exist
```

Fix:
- Create the key pair in AWS console or CLI.
- Use exact key name in `key_name`.
- Confirm region matches the key pair region.

### Bucket already exists

```text
BucketAlreadyExists: The requested bucket name is not available
```

Fix:
- Pick a unique global bucket name.
- Add random suffix or environment label.
- Use `terraform apply -var="bucket_name=my-unique-bucket-$(timestamp)"` in scripts.

### Permission denied

Fix:
- Avoid `sudo terraform ...` when not needed.
- Ensure the current user owns `.terraform`, `terraform.tfstate`, and `.terraform.lock.hcl`.
- On Linux, use `chmod` and `chown` if required.

### Other common issues

- `terraform init` after changing provider or backend.
- `Error: Unsupported block type` indicates invalid HCL.
- `ResourceAlreadyExists` means resource is partially present outside state.
- `Invalid index` or `Invalid for_each` means variable type mismatch.

### Debug workflow

```bash
terraform fmt -recursive
terraform validate
terraform plan -out=tfplan
terraform show tfplan
```

- Use `terraform graph` to understand resource dependencies.
- Use `terraform state list` and `terraform state show <resource>` to inspect tracked resources.
- Use `terraform refresh` if state drift occurs but use with caution.

---

## 9. Project Example (Mini Real-World)

### Goal
Provision an AWS EC2 instance plus an S3 bucket in one Terraform project.

### Recommended file structure

```text
31.Terraform/01-app/
  ├─ provider.tf
  ├─ backend.tf
  ├─ main.tf
  ├─ variables.tf
  ├─ outputs.tf
  ├─ terraform.tfvars
  ├─ locals.tf
  └─ README.md
```

### Example `provider.tf`

```hcl
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region  = var.aws_region
  profile = var.aws_profile
}
```

### Example `backend.tf`

```hcl
terraform {
  backend "s3" {
    bucket         = "my-terraform-state-bucket"
    key            = "31-terraform/01-app/terraform.tfstate"
    region         = "ap-south-1"
    dynamodb_table = "terraform-lock-table"
    encrypt        = true
  }
}
```

### Example `main.tf`

```hcl
resource "aws_instance" "web" {
  ami                    = var.ami_id
  instance_type          = var.instance_type
  key_name               = var.key_name
  associate_public_ip_address = true

  tags = {
    Name        = "terraform-demo-instance"
    Environment = var.environment
  }
}

resource "aws_s3_bucket" "app_bucket" {
  bucket = var.bucket_name
  acl    = "private"

  versioning {
    enabled = var.bucket_versioning
  }

  tags = {
    Name        = "terraform-demo-bucket"
    Environment = var.environment
  }
}
```

### Example `variables.tf`

```hcl
variable "aws_profile" {
  description = "AWS CLI profile"
  type        = string
  default     = "default"
}

variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "ap-south-1"
}

variable "ami_id" {
  description = "EC2 AMI ID"
  type        = string
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t3.micro"
}

variable "key_name" {
  description = "SSH key name"
  type        = string
}

variable "bucket_name" {
  description = "S3 bucket name"
  type        = string
}

variable "bucket_versioning" {
  description = "Enable S3 versioning"
  type        = bool
  default     = true
}

variable "environment" {
  description = "Deployment environment"
  type        = string
  default     = "dev"
}
```

### Example `terraform.tfvars`

```hcl
aws_profile      = "terraform-dev"
aws_region       = "ap-south-1"
ami_id           = "ami-0e12ffc2dd465f6e4"
instance_type    = "t3.micro"
key_name         = "TerraformKp"
bucket_name      = "my-terraform-demo-bucket-12345"
bucket_versioning = true
environment      = "dev"
```

### Example `outputs.tf`

```hcl
output "instance_id" {
  description = "EC2 instance ID"
  value       = aws_instance.web.id
}

output "instance_public_ip" {
  description = "EC2 public IP"
  value       = aws_instance.web.public_ip
}

output "bucket_name" {
  description = "Created S3 bucket name"
  value       = aws_s3_bucket.app_bucket.bucket
}
```

### Deploy commands

```bash
terraform init
terraform fmt -recursive
terraform validate
terraform plan -out=tfplan
terraform apply tfplan
```

### Teardown command

```bash
terraform destroy
```

---

## 10. Pro Tips

- Use Terraform when you want multi-cloud or multi-service infrastructure management.
- Use CloudFormation when you need AWS-native, service-specific coverage and you are fully committed to AWS.
- For production:
  - separate folders or workspaces for `dev`, `staging`, `prod`
  - remote state and state locking
  - versioned modules
  - CI/CD validation pipeline
- Keep module inputs minimal and outputs focused.
- Avoid `provisioner` blocks unless there is no alternative.
- Prefer `user_data` or configuration management tools for bootstrapping instances.
- Keep provider versions pinned to avoid accidental upgrades.

### Resume tips

Describe Terraform projects with outcomes and tooling:

- "Built AWS infrastructure with Terraform to provision EC2, S3, and IAM resources using remote state and automated plan/apply workflows."
- "Created reusable Terraform modules, enforced formatting with `terraform fmt`, and validated configs with `terraform validate`."
- "Managed environment isolation using Terraform workspaces and S3 backend state locking."

---

## Bonus: Day 1 Terraform Setup Flow

1. Install Terraform CLI.
2. Install AWS CLI.
3. Install VS Code and Terraform extension.
4. Create the project folder and files: `provider.tf`, `main.tf`, `variables.tf`, `outputs.tf`, `terraform.tfvars`.
5. Configure AWS credentials: `aws configure`.
6. Initialize the project: `terraform init`.
7. Format files: `terraform fmt -recursive`.
8. Validate config: `terraform validate`.
9. Preview plan: `terraform plan -out=tfplan`.
10. Apply: `terraform apply tfplan`.
11. Inspect outputs and verify resources in AWS console.
12. Destroy test resources when finished.

---

## Bonus: Interview Questions & Answers

| Question | Answer |
|---|---|
| What is Terraform? | Terraform is a declarative IaC tool that provisions infrastructure with code. |
| What is a provider? | A provider is a plugin that interacts with a specific service, like AWS or Azure. |
| What is the purpose of `terraform plan`? | It previews changes without applying them. |
| Why use remote state? | Remote state allows sharing, locking, and recovery between team members. |
| What is `terraform fmt` used for? | It formats Terraform code into a standard style. |
| When should you use `terraform import`? | To bring existing resources under Terraform management. |
| How do you avoid hardcoding secrets? | Use variables, environment variables, or secret stores instead of plain text. |
| What is `terraform workspace` for? | It isolates multiple states in the same config, typically for dev/staging/prod. |
| What does `terraform validate` do? | It checks syntax and configuration validity without creating resources. |
| How do you manage provider versions? | Use `required_providers` and version constraints in the Terraform block. |
### Example `terraform.tfvars`

```hcl
ami_id            = "ami-0e12ffc2dd465f6e4"
key_name          = "TerraformKp"
bucket_name       = "my-terraform-demo-bucket-12345"
```

### Deploy commands

```bash
terraform init
terraform fmt -recursive
terraform validate
terraform plan -out=tfplan
terraform apply tfplan
```

### Teardown command

```bash
terraform destroy
```

## 10. Pro Tips

- Use Terraform when you want cloud-agnostic, reusable declarative infrastructure.
- Use CloudFormation if you are purely AWS-native and want tight AWS service coverage.
- For production, split environments into `dev`, `staging`, `prod` folders with separate state/backends.
- Keep modules small and versioned.
- Use remote state locking and encryption.
- In a resume, describe Terraform projects like:
  - "Built IaC using Terraform to provision AWS EC2, S3 and VPC resources with remote state management."
  - "Created reusable Terraform modules and deployed infrastructure with automated `plan`/`apply` workflows."

## Bonus: Day 1 Terraform Setup Flow

1. Install Terraform CLI.
2. Install VS Code Terraform extension.
3. Create project folder and files: `main.tf`, `variables.tf`, `outputs.tf`, `terraform.tfvars`.
4. Configure AWS credentials with `aws configure`.
5. Run `terraform init`.
6. Run `terraform fmt -recursive`.
7. Run `terraform validate`.
8. Run `terraform plan -out=tfplan`.
9. Run `terraform apply tfplan`.
10. Verify resources in AWS console.

## Bonus: Interview Questions & Answers

| Question | Short answer |
|---|---|
| What is Terraform? | IaC tool for provisioning cloud infrastructure declaratively. |
| What is a provider? | A plugin that manages resources for a specific platform, like AWS. |
| Why use `terraform plan`? | To preview changes before applying them. |
| Why should state be remote? | To share state among team members and enable locking. |
| How do variables improve Terraform? | They make code reusable and avoid hardcoding values. |