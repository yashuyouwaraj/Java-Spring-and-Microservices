# 🐧 Complete Linux Command Cheatsheet

## Table of Contents
1. [Introduction](#introduction)
2. [Basic Commands](#basic-commands)
3. [File & Directory Operations](#file--directory-operations)
4. [File Viewing & Content](#file-viewing--content)
5. [Text Processing & Editing](#text-processing--editing)
6. [File Permissions & Ownership](#file-permissions--ownership)
7. [User & Group Management](#user--group-management)
8. [System Information](#system-information)
9. [Process Management](#process-management)
10. [Network Commands](#network-commands)
11. [Archive Operations](#archive-operations)
12. [File Search & Locate](#file-search--locate)
13. [Stream Redirection](#stream-redirection)
14. [Package Management](#package-management)
15. [Advanced Topics](#advanced-topics)

---

## Introduction

### What is Linux?
Linux is a free, open-source operating system kernel created by Linus Torvalds in 1991. It's Unix-like and powers everything from servers to smartphones. The command line (CLI/Terminal) is where you interact with Linux through commands.

### Linux Directory Structure
```
/           - Root directory (top of the hierarchy)
/home       - User home directories
/root       - Root user's home directory
/etc        - Configuration files
/var        - Variable data (logs, caches)
/usr        - User programs and libraries
/bin        - Essential command binaries
/sbin       - System administration binaries
/tmp        - Temporary files
/opt        - Optional software packages
/dev        - Device files
/lib        - System libraries
/boot       - Boot files
```

### Basic Concepts
- **Terminal/Shell**: Interface to communicate with the OS
- **Command**: Instruction given to the OS
- **Argument/Parameter**: Additional info for a command
- **Flag/Option**: Modifier for a command (usually starts with `-` or `--`)
- **Path**: Location of a file or directory (absolute or relative)

---

## Basic Commands

### 1. **whoami** - Display Current User
Shows the username of the currently logged-in user.

```bash
whoami
# Output: aman
```

### 2. **pwd** - Print Working Directory
Shows the full path of the current directory you're in.

```bash
pwd
# Output: /home/aman/linux
```

### 3. **clear** - Clear Terminal Screen
Clears all previous commands and output from the terminal.

```bash
clear
```

### 4. **echo** - Display Text
Prints text or variables to the terminal.

```bash
echo "Hello, Linux!"
# Output: Hello, Linux!

echo "My name is $USER"
# Output: My name is aman
```

### 5. **date** - Show Current Date & Time
Displays the current date and time.

```bash
date
# Output: Mon Apr 26 10:30:45 UTC 2026

date "+%d/%m/%Y"
# Output: 26/04/2026
```

### 6. **history** - Show Command History
Shows all previously executed commands with line numbers.

```bash
history
# Output: 
#   1  whoami
#   2  clear
#   3  pwd
#   ... (and so on)

history 10  # Shows last 10 commands
history -c  # Clears history
```

### 7. **man** - Manual Pages
Displays the manual (help documentation) for any command.

```bash
man ls
# Opens detailed documentation for 'ls' command
# Press 'q' to quit

man passwd
# Shows manual for passwd command
```

### 8. **exit** - Exit Terminal/Shell
Closes the terminal or logs out.

```bash
exit
```

---

## File & Directory Operations

### 1. **ls** - List Directory Contents

**Basic Usage:**
```bash
ls
# Lists all files and folders in current directory
```

**Common Flags:**
```bash
ls -l          # Long format (detailed info)
# Output:
# -rw-r--r-- 1 aman aman  256 Apr 26 10:30 info.txt
# drwxr-xr-x 2 aman aman 4096 Apr 26 10:15 aws

ls -a          # Show hidden files (starting with .)
# Output: . .. .bashrc .ssh desktop info.txt

ls -h          # Human-readable sizes (KB, MB, GB)
# Output: -rw-r--r-- 1 aman aman 256 Apr 26 info.txt

ls -r          # Reverse order
ls -lh         # Long format with human-readable sizes
ls -ltr        # Long format, sorted by time (newest last)
ls -lS         # Sort by size (largest first)
```

### 2. **cd** - Change Directory

```bash
cd /home/aman       # Absolute path - go to specific location
cd ..               # Go to parent directory
cd .                # Current directory
cd ~                # Go to home directory
cd -                # Go to previous directory
cd /                # Go to root directory
cd aman/documents   # Relative path - go to subdirectory
```

### 3. **mkdir** - Make Directory
Creates new directories.

```bash
mkdir yashu
# Creates a folder named 'yashu'

mkdir -p folder1/folder2/folder3
# Creates nested folders (parent folders created automatically)

mkdir dir1 dir2 dir3
# Creates multiple directories at once
```

### 4. **touch** - Create Empty File
Creates an empty file or updates file timestamp.

```bash
touch linux.txt
# Creates empty file 'linux.txt'

touch file1.txt file2.txt file3.txt
# Creates multiple files

touch aws.txt shell.txt
# Multiple files in one command
```

### 5. **rm** - Remove Files
Deletes files permanently.

```bash
rm linux.txt
# Removes file 'linux.txt'

rm *.txt
# Removes all files with .txt extension

rm file1.txt file2.txt file3.txt
# Removes multiple specific files

rm -f file.txt
# Force remove (doesn't ask for confirmation)

rm -i file.txt
# Interactive mode (asks before deleting)
```

### 6. **rmdir** - Remove Empty Directory
Removes only empty directories.

```bash
rmdir alien
# Removes directory 'alien' (only if empty)

rmdir yashu aws
# Removes multiple empty directories

rmdir -p dir1/dir2/dir3
# Removes nested empty directories
```

### 7. **rm -rf** - Remove Directory & Contents
Removes directories and all files inside them.

```bash
rm -rf yashu
# Removes 'yashu' directory and everything in it

rm -rf folder1/folder2
# Removes nested directory structure

# ⚠️ WARNING: Be very careful! This is permanent deletion
rm -rf /
# DON'T DO THIS! Deletes entire system!
```

### 8. **cp** - Copy Files & Directories

```bash
cp alien.txt info.txt
# Copies 'alien.txt' to 'info.txt'

cp file.txt /home/aman/documents/
# Copies file to another directory

cp -r folder1 folder2
# Copies entire folder and its contents (recursive)

cp file.txt file_backup.txt
# Creates a copy with different name
```

### 9. **mv** - Move or Rename Files

```bash
mv aws.txt azure.txt
# Renames 'aws.txt' to 'azure.txt'

mv aws.txt /home/aman/docs/
# Moves file to another directory

mv aws.txt aws/
# Moves file into a directory

mv aws.txt azure.txt /backup/
# Moves multiple files

mv folder1 folder2
# Renames or moves a folder
```

### 10. **pwd** - Show Current Path
```bash
pwd
# Output: /home/aman/workspace
```

---

## File Viewing & Content

### 1. **cat** - Concatenate & Display Files
Displays entire file contents.

```bash
cat info.txt
# Shows contents of info.txt

cat file1.txt file2.txt
# Displays multiple files sequentially

cat file1.txt file2.txt > combined.txt
# Combines files into one

cat > newfile.txt
# Creates file and waits for input (Press Ctrl+D to save)
# Example:
# $ cat > message.txt
# This is a new file
# Created in Linux
# Ctrl+D
```

### 2. **cat >>** - Append to File
Adds content to end of existing file.

```bash
cat >> info.txt
# Appends to existing file
# Input your text, press Ctrl+D when done

echo "New line" >> info.txt
# Appends text from echo command
```

### 3. **head** - Display First Lines
Shows first 10 lines by default.

```bash
head linux.txt
# Shows first 10 lines of file

head -n 5 linux.txt
# Shows first 5 lines specifically

head -n 2 linux.txt
# Output:
# Line 1 content
# Line 2 content
```

### 4. **tail** - Display Last Lines
Shows last 10 lines by default.

```bash
tail linux.txt
# Shows last 10 lines

tail -n 5 linux.txt
# Shows last 5 lines specifically

tail -n 2 linux.txt
# Output:
# Second last line
# Last line

tail -f logfile.txt
# Continuously shows new lines added (useful for logs)
```

### 5. **wc** - Word Count
Counts lines, words, and characters.

```bash
wc info.txt
# Output: 25 150 1024 info.txt
#         (lines, words, bytes)

wc -l info.txt
# Shows only line count: 25

wc -w info.txt
# Shows only word count: 150

wc -c info.txt
# Shows only byte count: 1024

wc -L info.txt
# Shows longest line length
```

### 6. **less/more** - Page Through Files
Allows scrolling through large files.

```bash
less largefile.txt
# Press Space for next page, 'q' to quit
# You can search with /keyword and navigate with arrow keys

more largefile.txt
# Similar to less but older
```

---

## Text Processing & Editing

### 1. **grep** - Search Text Pattern
Searches for specific text patterns in files.

```bash
grep "dumbbell" info.txt
# Finds lines containing "dumbbell"

grep -i "dumbbell" info.txt
# Case-insensitive search

grep -v "Dumbbell" info.txt
# Shows lines NOT containing "Dumbbell" (invert)

grep -c "Dumbbell" info.txt
# Counts how many lines contain "Dumbbell"
# Output: 3

grep -n "Dumbbell" info.txt
# Shows line numbers with matches

grep -r "pattern" folder/
# Recursively searches in all files in folder

grep "^error" log.txt
# Lines starting with "error"

grep "end$" log.txt
# Lines ending with "end"
```

### 2. **sed** - Stream Editor
Performs text substitution and manipulation.

```bash
sed 's/yashu/YashuAi/g' linux.txt
# Replace 'yashu' with 'YashuAi' (g = global, all occurrences)
# Does NOT modify original file

sed -i 's/yashu/YashuAi/g' linux.txt
# -i flag: saves changes to original file

sed 's/old/new/' file.txt
# Replaces first occurrence on each line

sed 's/old/new/2' file.txt
# Replaces second occurrence on each line

sed '4d' linux.txt
# Deletes line 4

sed -i '4d' linux.txt
# Permanently deletes line 4 from file

sed '$d' linux.txt
# Deletes last line

sed -i '$d' linux.txt
# Permanently deletes last line

sed -i '4i\new text here' linux.txt
# Inserts "new text here" before line 4

sed -i '$a\appended text' linux.txt
# Appends text at end of file

sed '3,5d' file.txt
# Deletes lines 3 to 5

sed -n '5,10p' file.txt
# Prints only lines 5 to 10
```

### 3. **awk** - Text Processing
Powerful tool for processing structured text.

```bash
awk '{print $1}' file.txt
# Prints first column

awk '{print $1, $3}' file.txt
# Prints 1st and 3rd columns

awk -F: '{print $1}' /etc/passwd
# Uses colon as field separator

awk '{sum += $1} END {print sum}' numbers.txt
# Sums numbers in first column
```

### 4. **vi/vim** - Text Editor
Interactive text editor.

```bash
vi filename.txt
# Opens file in vi editor

# In vi editor:
# Press 'i' to enter insert mode (edit text)
# Press 'Esc' to exit insert mode
# Type ':w' to save and ':q' to quit
# Type ':wq' to save and quit
# Type ':q!' to quit without saving
# Type 'dd' to delete a line
# Type 'u' to undo
# Type '/word' to search

vim filename.txt
# Modern version of vi with more features
```

### 5. **nano** - Simple Text Editor
User-friendly text editor.

```bash
nano filename.txt
# Opens file for editing
# Ctrl+O to save
# Ctrl+X to exit
# Use arrow keys to navigate
```

### 6. **sort** - Sort Lines
Sorts file contents.

```bash
sort file.txt
# Sorts alphabetically

sort -n numbers.txt
# Numerical sort

sort -r file.txt
# Reverse sort (Z to A)

sort -u file.txt
# Removes duplicates while sorting
```

### 7. **uniq** - Remove Duplicates
Removes consecutive duplicate lines.

```bash
uniq file.txt
# Removes adjacent duplicates

sort file.txt | uniq
# Sorts then removes all duplicates

uniq -c file.txt
# Shows count of duplicates

uniq -d file.txt
# Shows only duplicate lines
```

### 8. **cut** - Extract Columns
Extracts specific columns from text.

```bash
cut -d: -f1 /etc/passwd
# Extracts first field, using ':' as delimiter

cut -c 1-5 file.txt
# Extracts first 5 characters of each line

cut -d' ' -f2,4 file.txt
# Extracts 2nd and 4th fields, space-delimited
```

### 9. **tr** - Translate Characters
Replaces or removes characters.

```bash
tr 'a' 'b' < file.txt
# Replaces 'a' with 'b'

tr 'A-Z' 'a-z' < file.txt
# Converts uppercase to lowercase

tr -d 'a' < file.txt
# Removes all 'a' characters

echo "hello" | tr 'a-z' 'A-Z'
# Output: HELLO
```

### 10. **diff** - Compare Files
Shows differences between two files.

```bash
diff info.txt linux.txt
# Compares two files line by line
# Output shows different lines with markers:
# < lines in first file only
# > lines in second file only
# ---
# change indicators
```

---

## File Permissions & Ownership

### 1. Understanding Permissions
```
-rw-r--r--  1  aman  aman  256  Apr 26  10:30  info.txt
│││││││││  │  │     │     │    │   │     │
├─┤ owner  │  │     │     │    │   │     filename
│ ├─ group │  │     │     │    │   modification time
│ └─ other │  │     │     │    ownership info
│          │  link count
file type (- = file, d = directory)

Permissions breakdown (3 groups of 3):
r (read)    = 4
w (write)   = 2
x (execute) = 1

-rw-r--r--:
- owner: rw- (6 = 4+2)
- group: r-- (4)
- other: r-- (4)
= 644 in numeric format
```

### 2. **chmod** - Change File Permissions

```bash
chmod u+x alien.txt
# Adds execute permission to owner (user)

chmod o+w alien.txt
# Adds write permission to others

chmod o-w alien.txt
# Removes write permission from others

chmod 766 info.txt
# Sets permissions numerically:
# owner: 7 (rwx), group: 6 (rw-), other: 6 (rw-)

chmod 744 info.txt
# owner: 7 (rwx), group: 4 (r--), other: 4 (r--)
# Standard for scripts/executables

chmod 755 script.sh
# Executable by all, writable only by owner

chmod -R 755 folder/
# Recursively changes all files in folder

# Quick reference:
# chmod 777 - full permissions for everyone
# chmod 755 - owner full, others read+execute
# chmod 644 - owner write, all read
# chmod 600 - owner only, read and write
```

### 3. **chown** - Change Owner

```bash
sudo chown aman info.txt
# Changes owner to 'aman'

sudo chown aman:aman info.txt
# Changes owner to 'aman' and group to 'aman'

sudo chown -R aman folder/
# Recursively changes ownership of folder and contents
```

### 4. **chgrp** - Change Group

```bash
sudo chgrp yashu info.txt
# Changes group to 'yashu'

sudo chgrp -R yashu folder/
# Recursively changes group
```

---

## User & Group Management

### 1. **useradd** - Add New User
Creates a new user account.

```bash
sudo useradd aman
# Creates user 'aman'

sudo useradd -m -s /bin/bash deepika
# -m: creates home directory
# -s: sets shell to /bin/bash
```

### 2. **passwd** - Set/Change Password
Sets or changes user password.

```bash
sudo passwd aman
# Sets password for 'aman'
# Prompts you to enter new password twice

passwd
# Changes password of current user

sudo passwd -d aman
# Removes password (empty password)

sudo passwd -l aman
# Locks user account

sudo passwd -u aman
# Unlocks user account
```

### 3. **userdel** - Delete User
Removes a user account.

```bash
sudo userdel aman
# Deletes user but keeps home directory

sudo userdel -r aman
# -r: also deletes home directory and mail
```

### 4. **groupadd** - Add New Group
Creates a new group.

```bash
sudo groupadd yashu
# Creates group 'yashu'

sudo groupadd -g 1500 developers
# -g: specifies group ID
```

### 5. **usermod** - Modify User
Changes user account properties.

```bash
sudo usermod -aG yashu aman
# -a: adds (append) user to group
# -G: specifies group
# Adds user 'aman' to group 'yashu'

sudo usermod -s /bin/bash aman
# Changes default shell

sudo usermod -d /home/newdir aman
# Changes home directory

sudo usermod -l newname aman
# Changes username from 'aman' to 'newname'
```

### 6. **groupdel** - Delete Group

```bash
sudo groupdel yashu
# Deletes group 'yashu'
```

### 7. **id** - Show User Information

```bash
id
# Shows current user's ID and groups

id aman
# Shows user 'aman' information
# Output: uid=1001(aman) gid=1001(aman) groups=1001(aman),1500(yashu)

whoami
# Shows just the username
```

### 8. **su** - Switch User

```bash
su aman
# Switches to user 'aman' (prompts for password)

su - aman
# Switches and loads user's environment

su -
# Switches to root user
```

### 9. **sudo** - Execute as Administrator
Executes commands with superuser privileges.

```bash
sudo command
# Runs command as root/administrator

sudo -u aman command
# Runs command as user 'aman'

sudo -l
# Shows what commands current user can run as sudo

sudo -i
# Opens root shell
```

---

## System Information

### 1. **uname** - System Information

```bash
uname
# Output: Linux

uname -a
# All system information
# Output: Linux hostname 5.10.0 #1 SMP x86_64 GNU/Linux

uname -s
# Kernel name: Linux

uname -r
# Kernel release: 5.10.0-123-generic

uname -m
# Machine hardware: x86_64
```

### 2. **hostname** - Show Computer Name

```bash
hostname
# Output: mycomputer

sudo hostnamectl set-hostname newname
# Changes hostname
```

### 3. **uptime** - System Running Time

```bash
uptime
# Output: 10:45:23 up 45 days, 3:20, 2 users, load average: 0.12, 0.15, 0.10
```

### 4. **free** - Memory Usage

```bash
free
# Shows memory usage in KB

free -h
# Human-readable format (GB, MB, etc.)
# Output:
#              total        used        free      shared  buff/cache   available
# Mem:          7.8Gi       2.5Gi       2.1Gi      256Mi       3.2Gi       4.8Gi
# Swap:         2.0Gi          0B       2.0Gi

free -m
# Shows in megabytes
```

### 5. **df** - Disk Space

```bash
df
# Shows disk usage of all mounted filesystems

df -h
# Human-readable format
# Output:
# Filesystem      Size  Used Avail Use% Mounted on
# /dev/sda1       100G   45G   55G  45% /

df -i
# Shows inode usage instead of blocks
```

### 6. **du** - Directory Space Usage

```bash
du
# Shows size of current directory and subdirectories

du -h
# Human-readable format

du -sh folder/
# -s: summary only (total size)
# Output: 2.5G    folder/

du -h | sort -h
# Sorted output (smallest to largest)
```

### 7. **top** - Process Monitor (Interactive)

```bash
top
# Opens interactive process monitor
# Press 'q' to quit
# Shows: PID, user, CPU%, memory%, process name

# Column meanings:
# PID: Process ID
# USER: Process owner
# %CPU: CPU usage percentage
# %MEM: Memory usage percentage
# COMMAND: Process name

# Press:
# 1 - show CPU cores
# P - sort by CPU
# M - sort by memory
```

### 8. **ps** - Process Status

```bash
ps
# Shows processes for current user

ps aux
# Shows all processes with details
# Output:
# USER  PID  %CPU %MEM    VSZ   RSS TTY STAT START   TIME COMMAND
# root    1  0.0  0.1  18384  2480 ?   Ss   Apr26   0:01 /sbin/init

ps -ef | grep java
# Shows java processes

ps -p 1234
# Shows specific process by PID
```

### 9. **kill** - Terminate Process

```bash
kill 1234
# Sends SIGTERM to process 1234 (graceful shutdown)

kill -9 1234
# -9: SIGKILL (force kill)

killall firefox
# Kills all processes named 'firefox'

pkill -f 'process name'
# Kills processes by name pattern
```

---

## Network Commands

### 1. **ping** - Test Connectivity

```bash
ping www.google.com
# Sends ICMP packets to test connection
# Output:
# PING www.google.com (142.251.41.14) 56(84) bytes of data.
# 64 bytes from ord37s32-in-f14.1e100.net (142.251.41.14): icmp_seq=1 ttl=118 time=25.3 ms
# 64 bytes from ord37s32-in-f14.1e100.net (142.251.41.14): icmp_seq=2 ttl=118 time=24.8 ms
# Press Ctrl+C to stop

ping -c 4 www.google.com
# Sends only 4 packets then stops
```

### 2. **ifconfig** - Network Interface Configuration

```bash
ifconfig
# Shows all network interfaces and IP addresses

ifconfig eth0
# Shows specific interface information

ip addr
# Modern alternative to ifconfig
# Shows all IP addresses
```

### 3. **curl** - Fetch Data from URLs

```bash
curl https://api.github.com
# Fetches content from URL and displays in terminal

curl -O https://example.com/file.zip
# Downloads file

curl -o newname.zip https://example.com/file.zip
# Downloads with custom name

curl -L https://example.com
# Follows redirects

curl -X POST -d "data" https://api.example.com
# Sends POST request with data

curl -H "Authorization: Bearer token" https://api.example.com
# Adds custom header
```

### 4. **wget** - Download Files

```bash
wget https://pixabay.com/images/download/bessi-flower-729510_1920.jpg
# Downloads file from URL

wget -O filename.jpg https://example.com/image.jpg
# Downloads with custom name

wget -r https://example.com
# Recursively downloads entire website

wget -q https://example.com/file.zip
# Quiet mode (no output)

wget --limit-rate=100k https://example.com/file.zip
# Limits download speed to 100KB/s
```

### 5. **netstat** - Network Statistics

```bash
netstat -tuln
# Shows all listening ports
# -t: TCP, -u: UDP, -l: listening, -n: numeric

netstat -rn
# Shows routing table

netstat -p
# Shows which process owns the connection
```

### 6. **ss** - Socket Statistics

```bash
ss -tuln
# Modern replacement for netstat
# Shows all listening sockets

ss -p
# Shows process information

ss ESTABLISHED
# Shows established connections
```

### 7. **ssh** - Secure Shell

```bash
ssh user@hostname
# Connects to remote server via SSH

ssh -i /path/to/key.pem user@hostname
# Connects using private key file

ssh -p 2222 user@hostname
# Connects on specific port

ssh-keygen -t rsa -b 4096
# Generates SSH key pair

ssh-copy-id user@hostname
# Copies your public key to remote server (enables passwordless login)
```

---

## Archive Operations

### 1. **zip** - Create ZIP Archive

```bash
sudo zip devops *.txt
# Creates devops.zip containing all .txt files

sudo zip -r devops.zip folder/
# -r: recursive (includes subdirectories)

sudo zip devops.zip file1.txt file2.txt
# Archives specific files

sudo zip -sf devops.zip
# Lists contents of archive without extracting

sudo zip -q archive.zip file.txt
# Quiet mode (minimal output)

sudo zip -e archive.zip file.txt
# Encrypts the archive (prompts for password)
```

### 2. **unzip** - Extract ZIP Archive

```bash
unzip devops.zip
# Extracts all files from archive

unzip devops.zip -d /destination/
# -d: extracts to specific directory

unzip -l devops.zip
# -l: lists contents without extracting

unzip -t devops.zip
# -t: tests archive integrity

unzip archive.zip file.txt
# Extracts specific file only
```

### 3. **tar** - TAR Archive

```bash
tar -cvf archive.tar file1 file2 folder/
# -c: create, -v: verbose, -f: filename

tar -xvf archive.tar
# -x: extract

tar -czvf archive.tar.gz folder/
# -z: compress with gzip

tar -xzvf archive.tar.gz
# Extracts compressed tar

tar -tvf archive.tar
# -t: lists contents

tar -czf backup.tar.gz /home/aman/
# Backup directory structure
```

### 4. **gzip** - Compress Files

```bash
gzip file.txt
# Compresses to file.txt.gz (removes original)

gzip -k file.txt
# -k: keeps original file

gzip -d file.txt.gz
# Decompresses

gzip -9 file.txt
# -9: maximum compression
```

### 5. **bzip2** - Alternative Compression

```bash
bzip2 file.txt
# Compresses to file.txt.bz2

bzip2 -d file.txt.bz2
# Decompresses

bzip2 -k file.txt
# Keeps original file
```

---

## File Search & Locate

### 1. **find** - Search Files

```bash
find -name linux.txt
# Finds file in current directory

find /home -name linux.txt
# Searches in /home directory

find /home -type f -empty
# Finds all empty files
# -type f: files

find /home -type d -empty
# Finds all empty directories
# -type d: directories

find /home -name "*.txt"
# Finds all .txt files

find /home -size +10M
# Finds files larger than 10MB

find /home -name "*.log" -type f -mtime +7
# Finds .log files older than 7 days

find . -name "*.java" -exec grep -l "main" {} \;
# Finds Java files containing "main"

find . -type f -perm 644
# Finds files with specific permissions
```

### 2. **locate** - Fast File Search

Uses database (must be updated).

```bash
locate linux.txt
# Searches for linux.txt in database

locate "*.pdf"
# Searches for all PDF files

sudo updatedb
# Updates the locate database
# Usually runs automatically via cron

locate -i linux.txt
# Case-insensitive search
```

### 3. **whereis** - Locate Binary

```bash
whereis java
# Shows location of java executable

whereis grep
# Shows grep command location
```

### 4. **which** - Show Command Path

```bash
which java
# Shows full path to java command

which python3
# Shows python3 location
```

---

## Stream Redirection

### 1. **Output Redirection**

```bash
echo "Hello" > file.txt
# > : overwrites file

echo "World" >> file.txt
# >> : appends to file

command > output.txt 2>&1
# Redirects both output and errors

command 2> error.txt
# Redirects errors only

command > /dev/null
# Discards output
```

### 2. **Input Redirection**

```bash
cat < file.txt
# Provides file as input

sort < names.txt > sorted.txt
# Uses file as input, redirects output

grep "pattern" < data.txt
# Uses file as input to grep
```

### 3. **Piping**

```bash
cat info.txt | grep "error"
# Pipes output of cat to grep

ps aux | grep java
# Lists processes, filters for java

cat log.txt | tail -n 5
# Shows last 5 lines

history | grep "git"
# Searches command history

ls -la | sort -k5 -rn
# Lists files sorted by size
```

### 4. **Combining Operators**

```bash
command1 && command2
# Runs command2 only if command1 succeeds

command1 || command2
# Runs command2 only if command1 fails

(command1; command2) > output.txt
# Groups commands, redirects combined output

cat file1.txt file2.txt > combined.txt
# Combines two files

grep "error" *.txt | wc -l
# Counts error lines in all txt files
```

---

## Package Management

### 1. **APT** - Debian/Ubuntu Package Manager

```bash
sudo apt update
# Updates package list

sudo apt upgrade
# Upgrades all packages

sudo apt install package-name
# Installs a package

sudo apt remove package-name
# Removes a package

sudo apt autoremove
# Removes unnecessary dependencies

apt list --installed
# Lists all installed packages

sudo apt search keyword
# Searches for packages

apt show package-name
# Shows package information
```

### 2. **YUM** - Red Hat/CentOS Package Manager

```bash
sudo yum install git
# Installs git package

sudo yum update
# Updates system

sudo yum remove package-name
# Removes package

sudo yum search keyword
# Searches for package

yum list installed
# Lists installed packages
```

### 3. **DNF** - Modern Package Manager (Fedora/RHEL)

```bash
sudo dnf install maven
# Installs maven

sudo dnf update
# Updates system

sudo dnf remove package-name
# Removes package

sudo dnf search keyword
# Searches packages
```

### 4. **PIP** - Python Package Manager

```bash
pip install package-name
# Installs Python package

pip list
# Lists installed packages

pip upgrade package-name
# Upgrades package

pip uninstall package-name
# Removes package
```

---

## Advanced Topics

### 1. **Cron Jobs** - Schedule Tasks

```bash
crontab -e
# Opens cron job editor

crontab -l
# Lists all cron jobs

crontab -r
# Removes all cron jobs

# Cron syntax: minute hour day month weekday command
# 0 2 * * * /path/to/backup.sh
# Runs backup.sh daily at 2:00 AM

# Every day at 3:30 PM:
# 30 15 * * * command

# Every Monday at 8:00 AM:
# 0 8 * * 1 command

# Every 30 minutes:
# */30 * * * * command

# At reboot:
# @reboot command
```

### 2. **Symbolic Links**

```bash
ln -s /path/to/original link_name
# Creates symbolic link (shortcut)

ln original_file hard_link
# Creates hard link (duplicate reference)

ls -l
# Shows 'l' for symbolic links
```

### 3. **Bash Aliases**

```bash
alias ll='ls -lh'
# Creates shortcut 'll' for 'ls -lh'

alias grep='grep --color=auto'
# Adds default options

unalias ll
# Removes alias

alias
# Lists all aliases

# Make permanent: add to ~/.bashrc file:
# echo "alias ll='ls -lh'" >> ~/.bashrc
```

### 4. **Environment Variables**

```bash
echo $HOME
# Shows home directory

echo $PATH
# Shows directories where executables are searched

export MY_VAR="value"
# Creates environment variable

env
# Lists all environment variables

printenv | grep USER
# Shows specific variables
```

### 5. **Background & Foreground Jobs**

```bash
command &
# Runs command in background

Ctrl+Z
# Suspends current process

bg
# Resumes suspended job in background

fg
# Brings background job to foreground

jobs
# Lists all jobs

kill %1
# Kills job number 1
```

### 6. **Bash Scripting Basics**

```bash
#!/bin/bash
# Shebang line (defines shell)

# Variables
NAME="Linux"
echo "Hello $NAME"

# Conditionals
if [ $1 -gt 10 ]; then
    echo "Greater than 10"
else
    echo "Not greater"
fi

# Loops
for i in 1 2 3 4 5; do
    echo "Number: $i"
done

# While loop
while [ condition ]; do
    echo "Looping"
done

# Functions
greet() {
    echo "Hello $1"
}
greet "World"
```

### 7. **File Globbing**

```bash
*.txt          # All .txt files
file?.txt      # file1.txt, file2.txt, etc.
file[123].txt  # file1.txt, file2.txt, file3.txt
file[a-z].txt  # filea.txt to filez.txt
file{1,2,3}.txt # file1.txt, file2.txt, file3.txt
```

### 8. **Common Text Combinations**

```bash
# Find and replace in file
sed -i 's/old/new/g' file.txt

# Count lines in all files
wc -l *.txt

# Find large files
find . -size +1G

# Backup directory
tar -czf backup_$(date +%Y%m%d).tar.gz /home/aman

# Show disk usage
du -sh * | sort -h

# Find and delete
find . -name "*.tmp" -delete

# Monitor file changes
watch -n 1 'du -sh .'

# Check listening ports
sudo netstat -tlnp | grep LISTEN
```

---

## Quick Reference Card

| Task | Command |
|------|---------|
| Show location | `pwd` |
| Show user | `whoami` |
| List files | `ls -la` |
| Change directory | `cd /path` |
| Create folder | `mkdir foldername` |
| Create file | `touch filename` |
| View file | `cat filename` |
| First 10 lines | `head filename` |
| Last 10 lines | `tail filename` |
| Search text | `grep "pattern" filename` |
| Copy file | `cp source dest` |
| Move file | `mv source dest` |
| Delete file | `rm filename` |
| Delete folder | `rm -rf foldername` |
| Change permissions | `chmod 755 filename` |
| Edit file | `vi filename` |
| Find file | `find /path -name filename` |
| Zip files | `zip archive.zip file1 file2` |
| Unzip files | `unzip archive.zip` |
| Download file | `wget URL` |
| Test connection | `ping google.com` |
| Show memory | `free -h` |
| Show disk | `df -h` |
| Show processes | `ps aux` |
| Count lines | `wc -l filename` |
| Sort file | `sort filename` |
| Remove duplicates | `sort filename \| uniq` |

---

## Tips & Best Practices

### 1. **Safety First**
- Always backup important files before using `rm -rf`
- Test commands with `echo` first
- Use `-i` flag for interactive confirmation: `rm -i file.txt`

### 2. **Efficiency**
- Use TAB to auto-complete filenames
- Use `history` to repeat commands: `!1` repeats command 1
- Use aliases for frequently used commands
- Chain commands with pipes `|` and `&&`

### 3. **Debugging**
- Use `man command` to read documentation
- Add `-v` flag for verbose output
- Redirect errors: `command 2> error.txt`
- Use `echo` to debug variables

### 4. **File Organization**
- Keep scripts in a dedicated directory
- Use meaningful file names
- Organize by project/purpose
- Maintain backups

### 5. **Security**
- Don't run unnecessary `sudo` commands
- Restrict file permissions: `chmod 600` for sensitive files
- Use SSH keys instead of passwords
- Regularly update system: `sudo apt update && sudo apt upgrade`

---

## Conclusion

This cheatsheet covers the essential Linux commands for daily use. Linux is powerful and once you master these commands, you can automate many tasks and become highly productive. Practice regularly, explore `man` pages for detailed documentation, and don't be afraid to experiment (with backups first!).

**Remember**: The terminal is your best friend in Linux. Master it and you'll master the system.

---

## Additional Resources

- **Online Docs**: https://linux.die.net/ (man pages online)
- **Interactive Learning**: https://www.learnshell.org/
- **Practice**: Create projects and automate tasks
- **Community**: Join Linux forums and communities

**Happy Linux-ing! 🐧**

---
*Last Updated: April 2026*
*Comprehensive Linux Command Reference for Beginners and Intermediate Users*
