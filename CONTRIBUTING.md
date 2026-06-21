# Contributing to CASERO Android

Thanks for taking the time to contribute. This document explains how to propose
changes to the Android client.

## Ground rules

- Everything — code, comments, identifiers, documentation — is written in
  **English**.
- Be respectful. All participation is governed by our
  [Code of Conduct](CODE_OF_CONDUCT.md).
- Never commit credentials, signing keys, API tokens, or real guest data. If you
  ever paste a sample request, strip the username, password, cookies and any
  personal data first.

## Getting set up

```bash
git clone <this-repo-url>
cd casero-cu-apk
./gradlew assembleDebug
```

You need a recent Android Studio and JDK 17+.

## Branching

- `main` is always releasable.
- Branch from `main` using a descriptive prefix:
  - `feat/guest-list-paging`
  - `fix/session-expiry-redirect`
  - `docs/api-contract`

## Commits

This project uses [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(optional scope): <description>

[optional body]

[optional footer]
```

Common types: `feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`,
`build`, `ci`, `chore`.

Examples:

```
feat(guests): parse .NET /Date(ms)/ timestamps as UTC
fix(auth): resend anti-forgery cookie on every POST
docs: document the ViasComunicacion endpoint
```

Keep the subject line at 50 characters or fewer, in the imperative mood. Do not
add tool or AI attribution trailers.

## Pull requests

1. Make sure the project builds: `./gradlew assembleDebug`.
2. Run the checks: `./gradlew testDebugUnitTest lint`.
3. Fill in the [pull request template](.github/PULL_REQUEST_TEMPLATE.md).
4. Link any issue the PR closes (`Closes #123`).
5. Keep PRs focused — one logical change per PR.

## Reporting bugs and requesting features

Use the issue templates under
[.github/ISSUE_TEMPLATE](.github/ISSUE_TEMPLATE). For anything touching the
portal API, describe the request/response without including real credentials or
guest data.
