# HexagonTask
The task here was to create an app that displays randomly generated hexagon map. First click on a hexagon should highlight it, second click should highlight second hexagon and show the shortest path to the first one. Third click should highlight hexagon and reset the previously highlighted ones and the path between them. Also there should be the way to import/export map with highlighted hexagons and path to/from JSON.

In this app importing to JSON happens without creating any external file. It is saved as a String object during the application runtime. After saving this String is printed to console for possibility to see its content.
