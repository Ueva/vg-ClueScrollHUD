# 📜 Clue Scroll HUD for Vulengate

This is a simple mod for use on the [Vulengate](https://www.vulengate.com) Minecraft server. It displays information
about the [Clue Scrolls](https://wikijs.vulengate.com/en/Cluescrolls) in your inventory directly on your HUD. Using this
mod, you'll no longer have to open your inventory to check your progress towards completing your clues!
<p align="center">
    <img alt="Animated .gif showing the Vulengate Clue Scroll HUD mod being used." height="399" src="assets/demo.png" width="392"/>
</p>

### Disclaimer

**This project is not developed by Vulengate. It is an unofficial, fan-made mod.**

Please do **not** contact Vulengate staff for support with this mod, as they are not responsible for it in any way.

[Vulengate staff have given consent](assets/vg-consent.png) for this modification to be developed and
distributed.

### Copyright

The *Vulengate* name, logo, and all associated content are the property of their respective owners. No copyrighted,
trademarked, or otherwise restricted assets have been used in the development of this mod.

This project is licensed under the [MIT license](LICENSE).

---

## Usage

This is a very simple mod, so getting up and running should be a breeze!

Below, you'll find guidance about (1) what dependencies you need to install, (2) how to download this mod, (3) how to
install this mod, (4) how to use this mod in-game, and (5) how to configure it to your liking.

### 1. Installing Dependencies

This mod has the following hard dependencies:

- Fabric [Loader](https://fabricmc.net) and [API](https://modrinth.com/mod/fabric-api)
- [Cloth Config API](https://modrinth.com/mod/cloth-config)

This mod also has an optional dependency on the [Mod Menu](https://modrinth.com/mod/modmenu) mod. <br>
Although Mod Menu is not required, it will make configuring this mod much easier.

### 2. Downloading the Latest Release

For most users, the easiest way to download this mod is via [Modrinth](https://modrinth.com/project/vg-cluescrollhud/).

Some users may prefer to download the latest release directly from GitHub. You can find the all pre-built `.jar`
releases on the [Releases tab](https://github.com/Ueva/vg-ClueScrollHUD/releases) of this repository.

Alternatively, you can download the source code and build the mod yourself. If this is something you are interested in,
you probably already know what you're doing; I won't include instructions about how to do so here.

### 3. Installing the Mod

The mod is installed like any other Fabric mod. Simply place the `.jar` file in your `/mods/` folder and launch the
game.

*Easy peasy lemon squeezy!*

### 4. Using the Mod

Once installed, you should see a new HUD element in the top-left of your screen when you next join Vulengate. The mod
requires no manual action on your part - it will display information about your current cluescrolls automatically.

The three default keybinds for this mod are:

- `'` - Toggle the Clue Scroll HUD.
- `[` - Show the previous clue scroll in your inventory.
- `]` - Show the next clue scroll in your inventory.

### 5. Configuring the Mod

This mod's keybinds can be changed in the Controls menu.

There are a few configuration options that can be changed via Mod Menu.
If you have not installed Mod Menu, you can edit the configuration file (`/config/vg-cluescrollhud.toml`)
directly.

---

## Getting Involved

In general, contributions to this project are more than welcome! In particular, I am committed to ironing out any bugs
or unintended behaviour, and to adding features that the community would find useful.

However, please bear in mind that this project is developed in my free time, on a voluntary basis. I will do my best
to respond to issues and pull requests in a timely manner, but I cannot make any guarantees.

### Contributing Code

If you would like to contribute to this project, please fork the repository, make your changes, and submit a pull
request.
Please ensure that your code is well-documented and that you have followed the contribution guidelines.

### Reporting Bugs

If you would like to report a bug, please [open an issue](https://github.com/Ueva/vg-ClueScrollHUD/issues) on the
GitHub repository.
Please do not contact any developers or maintainers directly.

### Requesting Features

If you would like to request a feature, please [open an issue](https://github.com/Ueva/vg-ClueScrollHUD/issues) on the
GitHub repository and tag it as a feature request.
Please do not contact any developers or maintainers directly.

---

## FAQs

Here, you will find answers to a collection of frequently asked questions about this mod.

### In-Game FAQs

This section addresses using the mod in normal gameplay on the Vulengate server.

#### Is this mod allowed on Vulengate?

Yes! This mod is permitted under Vulengate's server rules. It does not give you any unfair advantage over other players,
and it has no potential to degrade server performance.

I have also received [explicit permission](assets/vg-consent.png) from Vulengate staff to develop and distribute this
mod.

#### Why isn't my task progress being updated immediately on the HUD?

This mod reads your progress directly from the clue scroll items in your inventory - the HUD will update as soon as the
scroll items themselves are updated. This is usually immediate, but there may be a slight delay for certain types
of clues. Movement-related tasks (e.g., "Walk..." or "Ride...") seem particularly prone to this.

This isn’t a bug with this mod. Rather, it is a limitation of how the clue scroll plugin works on the Vulengate server.
Once the server registers your progress and updates the scroll's data, the HUD will reflect it immediately after.

### Development FAQs

This section addresses broader questions about the mod’s development, feature requests, compatibility, and future plans.

#### Can you port this mod to Forge/NeoForge?

Maybe in the future, but not any time soon.

I've not developed a Forge mod for over a decade, so I don't have any plans to do this any time soon.
If you'd like to contribute a Forge port, please get in touch.

**Note**: It may be possible to use this mod on NeoForge using
the [Sinytra Connector](https://modrinth.com/mod/connector)
mod.

#### Can you port this mod to Bedrock Edition?

This is orders of magnitude less likely than a Forge port. I've honestly never even booted up Bedrock Edition, let alone
developed an addon for it. I don't intend on starting now.

My advice: use the Java edition of Minecraft. Be like the cool kids back in 2009.

#### Can you update to [version]?

Yes! I will do my very best to keep this mod up to date with the latest version of Minecraft used on Vulengate.

Sometimes, this may take a little while, especially if there are breaking changes in (1) the Fabric or Minecraft APIs,
or (2) the clue scroll plugin used on Vulengate.

#### Can you fix [bug]?

Probably! Please [open an issue](https://github.com/Ueva/vg-ClueScrollHUD/issues) on the GitHub repository and tag it as
a bug report.

Bug fixes take priority over feature additions, so I'll try to implement them as soon as I can.

#### Can you add [feature]?

Maybe! If you have a feature request, please [open an issue](https://github.com/Ueva/vg-ClueScrollHUD/issues) on the
GitHub repository and tag it as a feature request.

The main things that will influence whether or not a feature is added are (1) how useful it is to the community in
general, and (2) how much free time I have to implement it.

Oftentimes, the fastest way to get a feature added is to implement it yourself and submit a pull request!

#### Why hasn't my issue been responded to/pull request been merged?

The most likely reason is that I haven't had time to look at it yet.
I'm a busy person, and I develop this project in my (often very limited) free time.
I'll do my best to respond to issues and pull requests in a timely manner, but I can't make any guarantees when it comes
to timeframes.

To increase the chances of your issue being responded to quickly, please ensure you have followed the provided
templates for reporting bugs or submitting feature requests. Similarly, please ensure you follow the contribution
guidelines when making a pull request.