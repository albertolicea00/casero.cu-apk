# Pinned certificate

The app pins the `casero.rem.cu` server certificate instead of trusting any CA
(the portal serves a certificate that fails standard validation).

Drop the DER/PEM certificate here as **`casero_rem_cu.cer`**. Until it is present
the client fails closed and refuses to connect.

Extract it from inside Cuba with:

```bash
openssl s_client -connect casero.rem.cu:443 -servername casero.rem.cu </dev/null \
  | openssl x509 -outform der -out casero_rem_cu.cer
```

Verify the certificate before bundling it. See [CLAUDE.md](../../../../CLAUDE.md).
