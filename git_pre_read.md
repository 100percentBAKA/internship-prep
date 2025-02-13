# VS Code Settings

## Set Git Bash as Default Terminal:
1. Go to **Settings**.
2. Search for **"default terminal windows"**.
3. Select **Git Bash** as the default terminal.

## Show .git Folder in File Explorer:
1. Open **Settings** (`Ctrl + ,`).
2. Search for **Files: Excluded**.
3. Remove `**/.git` from the exclusion list.

# Git Linking with GitHub on Windows

## Install Git:
- Download and install Git from [git-scm.com](https://git-scm.com/).

## Generate SSH Key:
```sh
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```
- Press **Enter** to accept the default file location.
- Leave passphrase empty (**optional**).

## Add SSH Key to GitHub:
1. Copy the SSH key:
   ```sh
   cat ~/.ssh/id_rsa.pub
   ```
2. Go to **GitHub Profile > Settings > SSH and GPG keys > New SSH Key**.
3. Paste the key and save.

## Test Connection:
```sh
ssh -T git@github.com
```
- You should see a success message.

# Git Commands

### Check Git Version:
```sh
git -v
```

## Git Config Commands

```sh
git config --global user.name "<GitHub_username>"
git config --global user.email "<GitHub_email>"
```
- This sets up your Git username and email.
- (You can use different usernames and email addresses, but this affects commit history.)

```sh
git config --global init.defaultBranch main
```
- Change the default branch from `master` to `main`.

```sh
git help config
```
- Launch browser Git help page.

# Git Normal Commands

```sh
git status        # Show the status of tracked and untracked files
git status -s     # Show the status of files in a shorter format
git add .         # Stage all modified files
git commit -m "<commit_message>"   # Commit all staged files
git commit -a -m "<commit_message>"  # Stage all files and commit them
```

# Git Restore Commands

```sh
git restore <file>            # Discard changes in the working directory for a specific file
git restore --staged <file>   # Unstage a file without affecting its content
git restore .                 # Restore all files to the last committed state
git restore --source=HEAD~1 <file>  # Restore a file to its state in the previous commit
```

# Git Commit Message Convention

```plaintext
<type>(optional scope): <description>
```

### Examples:
```plaintext
feat: add login functionality
fix(auth): resolve password validation issue
chore: update project dependencies
```

### Common Types:
- **feat**: A new feature.
- **fix**: A bug fix.
- **docs**: Documentation changes.
- **style**: Code style changes (formatting, no logic changes).
- **refactor**: Code restructuring without functional changes.
- **test**: Adding or updating tests.
- **chore**: Maintenance tasks (e.g., updating dependencies).
