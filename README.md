# MbtaCraftLivemap

MbtaCraftLivemap is a Paper-based Minecraft server plugin that renders a livemap of the MBTA.

This project is licensed under the [MIT License](LICENSE).

## Overview

- Rendered using data and real-time events from the [MBTA V3 API](https://www.mbta.com/developers/v3-api)
- As a server plugin, no client-side mods are required to see the map
- Right-click map elements - specifically for routes, stops, and vehicles - to view their data in chat
- Right-click the canvas with various items to toggle map element visibility:
    - Compass: Routes
    - Redstone Torch: Stops
    - Minecart: Vehicles

This project isn't really meant to be practical by any means, and instead serves as a fun and unorthodox way to
visualize and interact with transit data.

## Usage

1. Drag and drop the plugin JAR into the `plugins` folder at the root of your Paper server directory
2. In `plugins/MbtaCraftLivemap/config.yml`, set `api.key` to your MBTA V3 API key
   - You can request one for free at [their portal](https://api-v3.mbta.com/)

## Build

Make sure you have some JDK 21 installed on your machine, then run:

```
./gradlew build
```

This will output the plugin JAR in `build/libs`.