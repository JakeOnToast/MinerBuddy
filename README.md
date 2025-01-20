# Miner Buddy

## Context

I created this project in late 2020 before getting my gap year job.
I was still relatively new to Java and android development but I had gained a passion for making simple 2d games. I spent about a month working on this amongst other things and then stopped when I landed my first job.

I enjoyed solving mathematical problems and wanted to make the game run efficiently. I also wanted to do everything from scratch without the assistance of powerful libraries and game engines.
Initially I tried to make the game using a canvas and basic android components but the performance was awful and it wasn't the right thing to do. I looked into other solutions such as rendering the game using OpenGL but this ended up being more work than it was worth because I also needed to get interactions working seamlessly. I ended up using a barebones game engine that allowed me to efficiently render sprites on the screen and easily receive inputs from the user.

I solved many seemingly easy problems that turned out to be fairly complex due to the importance of the user experience feeling right.
For example zooming in and out seems simple but actually making it feel natural takes a lot of careful consideration.

I spent a lot of time creating the graphics. I'm not exactly good at art so this took way longer than I want to admit. After lots of experimentation I ended up making very simple geometric graphics.

## Graphics :eyes:

I decided to break each block down and only render what was necessary. For example the sides only need to be rendered if the block is on the edge. It probably would have actually been more efficient to have the entire block as one sprite and draw the sprites from top to bottom but I enjoyed the problem solving involved with doing it this way.

### In game example

#### World

![World screenshot](assets\world_screenshot.jpg)

#### Shop

![Shop screenshot](assets\shop_screenshot.jpg)

### Dirt

<p align="center">
  <img src="assets\raw_textures\tiles\dirtleft.png" width="15%" />
  <img src="assets\raw_textures\tiles\dirtfull.png" width="30%" />
  <img src="assets\raw_textures\tiles\dirthole.png" width="30%" />
  <img src="assets\raw_textures\tiles\dirtright.png" width="15%" />
</p>

### Stone

<p align="center">
  <img src="assets\raw_textures\tiles\stoneleft.png" width="15%" />
  <img src="assets\raw_textures\tiles\stonefull.png" width="30%" />
  <img src="assets\raw_textures\tiles\stonehole.png" width="30%" />
  <img src="assets\raw_textures\tiles\stoneright.png" width="15%" />
</p>

### Buttons

<p align="center">
  <img src="assets\raw_textures\buttons\green_button_up.png" width="22%" />
  <img src="assets\raw_textures\buttons\green_button_down.png" width="22%" />
  <img src="assets\raw_textures\buttons\red_button_up.png" width="22%" />
  <img src="assets\raw_textures\buttons\red_button_down.png" width="22%" />
</p>

