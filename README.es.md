# 🏠 CASERO.cu — Android
Cliente nativo Android para que los arrendadores de casas particulares en Cuba reporten sus huéspedes a las autoridades. 🇨🇺

[![License: MIT](https://img.shields.io/badge/Licencia-MIT-yellow.svg)](LICENSE)
[![Plataforma: Android](https://img.shields.io/badge/Plataforma-Android-3DDC84?logo=android)](https://developer.android.com)
[![Lenguaje: Kotlin](https://img.shields.io/badge/Lenguaje-Kotlin-7F52FF?logo=kotlin)](https://kotlinlang.org)
[![Build: Gradle KTS](https://img.shields.io/badge/Build-Gradle_KTS-02303A?logo=gradle)](https://gradle.org)
[![PRs: Bienvenidos](https://img.shields.io/badge/PRs-bienvenidos-brightgreen)](CONTRIBUTING.md)

[See the English version](README.md)

## ⚠️ Disclaimer

> [!WARNING]
> **Unofficial project.** Not affiliated with CIDP-MININT or any government entity. Talks to the official portal using the host's own credentials, the same way a browser does. We are not responsible for changes to `casero.rem.cu`, USSD/SMS codes, or any issues arising from using this software. Use at your own risk. Always verify guest registrations through official channels.


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

## 🏗 ¿Por qué aplicaciones nativas independientes? (iOS vs Android)

En lugar de utilizar una tecnología híbrida o multiplataforma (como Flutter o React Native), CASERO.cu mantiene bases de código nativas e independientes:

- **Permisos y libertad en Android:** Android permite un nivel más profundo de integración con el sistema operativo (automatización de llamadas/USSD, tareas en segundo plano e integración con el hardware), lo que permite extender el funcionamiento de la app según las necesidades.
- **Restricciones del Sandbox de iOS:** iOS aplica un sandbox más estricto y con menores libertades de API, por lo que la app en iOS se mantiene acotada a lo que la plataforma permite de forma oficial.
- **Experiencia optimizada:** El desarrollo nativo en Kotlin/Jetpack Compose para Android y Swift/SwiftUI para iOS garantiza el máximo rendimiento, una interfaz acorde a cada sistema y el cumplimiento de requerimientos de seguridad nativos (como certificate pinning y background workers).

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
- [casero.cu-web](https://github.com/albertolicea00/casero.cu-web) — Cliente Web 

## 📄 Licencia

[MIT](LICENSE) © 2026 Alberto Licea
