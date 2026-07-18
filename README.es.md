# 🏠 CASERO.cu — Android

[![License: MIT](https://img.shields.io/badge/Licencia-MIT-yellow.svg)](LICENSE)
[![Plataforma: Android](https://img.shields.io/badge/Plataforma-Android-3DDC84?logo=android)](https://developer.android.com)
[![Lenguaje: Kotlin](https://img.shields.io/badge/Lenguaje-Kotlin-7F52FF?logo=kotlin)](https://kotlinlang.org)
[![Build: Gradle KTS](https://img.shields.io/badge/Build-Gradle_KTS-02303A?logo=gradle)](https://gradle.org)
[![PRs: Bienvenidos](https://img.shields.io/badge/PRs-bienvenidos-brightgreen)](CONTRIBUTING.md)

[See the English version](README.md)

Cliente nativo Android para que los arrendadores de casas particulares en Cuba reporten sus huéspedes a las autoridades. 🇨🇺

> ⚠️ **Proyecto no oficial.** No está afiliado con CIDP-MININT ni ninguna entidad gubernamental. Se conecta al portal oficial usando las credenciales del propio arrendador, igual que lo haría un navegador.
>
> 🛡️ **Aviso:** No nos responsabilizamos por cambios en `casero.rem.cu`, los códigos USSD/SMS, o cualquier problema derivado del uso de este software. Úselo bajo su propio riesgo. Siempre verifique los registros de huéspedes por los canales oficiales.

---

## ✨ Funcionalidades

- 📋 **Registro de huéspedes** por dos vías:
  - 📞 **Códigos USSD / SMS** — funciona sin conexión a internet
  - 🌐 **API del portal web** — se autentica contra `casero.rem.cu` y replica las peticiones del navegador
- 👥 Lista de huéspedes activos y registrados, con acompañantes
- 📱 Gestión de vías de comunicación (teléfonos y correos)

## 🛠 Stack Tecnológico

| Capa | Opción |
|------|--------|
| Lenguaje | Kotlin |
| Build | Gradle (Kotlin DSL) |
| Red | Portal ASP.NET MVC (token antifalsificación + cookies de sesión) |

## 🚀 Primeros Pasos

```bash
git clone https://github.com/albertolicea00/casero.cu-apk.git
cd casero.cu-apk
./gradlew assembleDebug
```

Instalar en dispositivo:

```bash
./gradlew installDebug
```

> 🔐 El material de firma (`*.jks`, `keystore.properties`) nunca se sube al repositorio.

## 🔒 Nota sobre TLS

El portal sirve un certificado que no pasa la validación estándar (`net::ERR_CERT_AUTHORITY_INVALID`). La app usa **certificate pinning** — no se deshabilita TLS globalmente. Vea [CLAUDE.md](CLAUDE.md) para el flujo de peticiones invertido.

## 🤝 Contribuir

Vea [CONTRIBUTING.md](CONTRIBUTING.md), [SECURITY.md](SECURITY.md) y [Código de Conducta](CODE_OF_CONDUCT.md). Usamos [Conventional Commits](https://www.conventionalcommits.org/).

### 🐛 Reportar Bugs

¿Encontró un bug? Abra un issue en el repo correspondiente:
- **Bugs específicos de Android** → [abrir aquí](https://github.com/albertolicea00/casero.cu-apk/issues)
- **Bugs específicos de iOS** → [abrir aquí](https://github.com/albertolicea00/casero.cu-ios/issues)
- **Problemas core / multiplataforma** (cambios en la API, flujo de autenticación, etc.) → abrir en cualquiera de los dos, lo trackingeamos en ambos

## 📦 Repos Relacionados

- [casero.cu-ios](https://github.com/albertolicea00/casero.cu-ios) — Cliente iOS
- [casero.cu-apk](https://github.com/albertolicea00/casero.cu-apk) — Cliente Android (este repo)

## 📄 Licencia

[MIT](LICENSE) © 2026 Alberto Licea