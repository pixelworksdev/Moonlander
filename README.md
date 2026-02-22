# MoonLander

## Project Overview

**MoonLander** is a 2D lunar lander game where the player must safely land on procedurally generated moon terrain.  
The game combines **physics simulation**, **resource management (fuel)**, player controls, and an optional **autopilot** to create a fun and challenging experience.

---

## Gameplay

- The player controls the lander using **W/A/S/D** keys.
- **Thrust consumes fuel**; fuel is limited and crucial for success.
- The terrain is **procedurally generated** and contains **multiple landing zones**.
- Safe landings must occur on a **landing zone** at **low velocities**.
- The game ends if the lander **crashes** (hits terrain outside landing zones or with excessive speed) or **runs out of fuel**.
- Game status is clearly displayed: `"LANDED SUCCESSFULLY"` or `"GAME OVER - CRASHED"`.
- **Restart** is always possible with the **R key**.

---

## Features

- **Procedural Terrain** – Terrain is generated dynamically with multiple landing zones.
- **Landing Zones** – Specific flat areas where safe landings are possible.
- **Physics & Gravity** – Vector-based acceleration, velocity, and position updates.
- **Fuel System** – Thrusters consume fuel; running out ends the game.
- **Safe Landing Detection** – Landing zones plus speed thresholds determine success.
- **HUD** – Shows fuel, elapsed time, and game status.
- **Debug Mode** – Optional display of velocity, acceleration, and hitboxes for development purposes.
- **Autopilot (optional)** – Can control vertical speed automatically.

---

## Technical Implementation

- **Language:** Java
- **Graphics/UI:** Graphics2D
- **Architecture:** MVC-like
  - **Game** → Controller / Main Logic
  - **Moonlander** → Model (Physics, Status, Fuel)
  - **Terrain** → Model (Procedural Surface + Landing Zones)
- **Game States:** RUNNING, LANDED, CRASHED, PAUSED
- **Physics:** Vector-based for movement and acceleration
- **Collision Detection:** Lander vs terrain and landing zones

---

## Learning Goals

- Implementing **2D procedural terrain generation**.
- Working with **vectors for physics simulation**.
- Handling **resource management** (fuel).
- Managing **game state transitions** (running, landed, crashed, paused).
- Creating **HUD elements**, debug overlays, and restart functionality.
- Optional: implementing **autopilot logic**.
