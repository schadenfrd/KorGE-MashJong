This directory contains resources that are synchronized with the iOS App Bundle.

Files copied manually from `game/src/commonMain/resources`:
- atlas_classic_mahjong.png

These files are required for Korge's resourcesVfs to work on iOS, as the `game` module's resources are not automatically embedded into the App Bundle by the standard Compose Multiplatform framework build process.

If you update the atlas in `game` module, you MUST update this copy as well.
