# quick summaries
summary(test_set$V1) # or vector/list/...
boxplot(test_set$V1)
# histogram with "rug"
hist(test_set$V1, col = "snow3", breaks = 20) # see also colors(), rgb()
rug(test_set$V1) # shows points under the histogram
# boxplot overlain with a line 
boxplot(test_set$Activity, col = rgb(0.5, 1, 1))
abline(h=3.5, col = rgb(0, 1, 1))
# barplot
barplot(table(test_set$Activity), col = "wheat", main = "Number of measurements per activity")
# multiple boxplots
boxplot(Sepal.Length ~ Species, data = iris)
# multiple histograms
par(mfrow = c(2,1), mar = c(4, 4, 2, 1)) # sets space for two plots + margins for each
hist(subset(iris, Species == "virginica")$Sepal.Length)
hist(subset(iris, Species == "versicolor")$Sepal.Length)
# scatterplot
with(iris, plot(Sepal.Length, Sepal.Width)) # for one species
abline(v = 6, lwd = 2, lty = 2) # just for fun, here it's no specific threshold
with(iris, plot(Sepal.Length, Sepal.Width, col = Species)) # different colors for Species

# Plotting - Base Plotting System
library(datasets)
data(cars)
with(cars, plot(speed, dist)) # how long it takes for car to stop at given speed
with(cars, plot(dist ~ speed)) # the same using formula (order is reversed!)
plot(cars$speed, cars$dist) # the same, but axis labels read "cars$speed", etc.

# Plotting - Lattice Plot
library(lattice)
state <- data.frame(state.x77, region = state.region)
xyplot(Life.Exp ~ Income | region, data = state, layout = c(4, 1))

# Plotting - ggplot2 System
library(ggplot2)
data(mpg)
qplot(displ, hwy, data = mpg)

# Important graphics parameters
# pch: plotting symbol (by default open circle)
# lty: line type (default is solid)
# lwd: line width
# col: plotting color (see colors() for more)
# xlab/ylab: string for x/y-axis labels
# las: orientation of the axis labels
# bg: background color
# mar: margin size (bottom, left, top, right)
# oma: outer margin size
# mfrow: number of plots per row, column (c(2,1)) - plots go row-wise
# mfcol: number of plots per row, column - plots go column-wise
par("mar") # returns "mar" parameter, par() returns all, par can also set

# Base Plotting Functions:
# plot, lines, points, text, title, mtext, axis, legend

# Example of adding to initially empty plot
x <- rnorm(100)
y <- x+ rnorm(100)
g <- gl(2, 50, labels = c("Male", "Female"))
plot(x, y, type = "n") # prepares the plot, but draws no data
points(x[g == "Male"], y[g=="Male"], col = "green")
points(x[g == "Female"], y[g=="Female"], col = "blue", pch=19)
title("Males and Females")

# Lattice Plotting System
library(lattice)
# xyplot, bwplot, histogram, stripplot, dotplot, splom, levelplot, contourplot
xyplot(y ~ x | f * g, data) # f, g are categorical variables
library(datasets)
xyplot(Ozone ~ Wind, data = airquality)
airquality <- transform(airquality, Month = factor(Month))
xyplot(Ozone ~ Wind | Month, data = airquality, layout = c(5,1))
# lattice panel functions
set.seed(10)
x <- rnorm(100)
f <- rep(0:1, each = 50)
y <- x + f - f * x + rnorm(100, sd = 0.5)
f <- factor(f, labels = c("Group 1", "Group 2"))
xyplot(y ~ x | f, layout = c(2, 1))
xyplot(y ~ x | f, panel = function(x, y, ...) {
    panel.xyplot(x, y, ...) # creates the plot itself (for each panel)
    panel.abline(h = median(y), lty = 2) # adds ab-line for mean value
    panel.lmline(x, y, col = 2) # adds linear model (regression line)
})
xyplot(x, y)

# ggplot2 http://ggplot2.org
library(ggplot2)
# plots are made up of aesthetics (size, shape, color) and geoms (points, lines)
# use factors for categories, they should be labeled
?ggplot # core function, can do many things qplot function hides and simplifies
str(mpg) # mpg comes with ggplot2 package
qplot(displ, hwy, data = mpg, color = drv) # auto legend for colors of the factor variable drv
# add the points (default) and smooth (loess - see LOcal regrESSion for more)
qplot(displ, hwy, data = mpg, geom = c("point", "smooth"))
qplot(hwy, data = mpg, fill = drv) # histogram (because of one variable), but with drv colors
# facets are like panels
# split based on drv factor, . is for rows and results in 1 row
qplot(displ, hwy, data = mpg, facets = . ~ drv)
qplot(hwy, data = mpg, facets = drv ~ ., binwidth = 2) # drv rows times . (=1) column
# ggplot components:
# data frame
# aesthetic mappings - how data are mapped to color, size
# geoms - geometric objects like points, lines, shapes
# facets - conditional plots (panels)
# stats - statistical transformations like binning, quantiles, smoothing
# scales - what scale an aesthetic map uses (male = blue, female = red, etc.)
# coordinate system
g <- ggplot(mpg, aes(displ, hwy))
sum(g)
print(g) # Error: No layers in plot
g + geom_point() # this autoprints the plot, it has "layers" :-)
# smoother: + geom_smooth() or + geom_smooth(method = "lm") for linear model
# facets: + facet_grid(. ~ bmicat) # creates two panels by bmicat (2 level factor)
# Annotations
# labels: xlab(), ylab(), labs(), ggtitle()
# others: theme(legend.position = "none"), theme_gray() (default), theme_bw()
g + geom_point(color = "steelblue", size = 4, alpha = 0.5)
g + geom_point(aes(color = drv), size = 4, alpha = 0.5) # different colors based on drv
g + geom_point() + labs(title = "hwy by displ", x = "displacement")
g + geom_point() + geom_smooth(size = 4, linetype = 3, method = "lm", se = FALSE) # se = F: confidence bands off
g + geom_point() + theme_bw(base_family = "Times") # BW theme + font (not working on Windows as-is)
# Axis limits - example with outlier
testdata <- data.frame(x = 1:100, y = rnorm(100))
testdata[50,2] <- 100 # outlier
plot(testdata$x, testdata$y, type = "l", ylim = c(-3, 3)) # outlier goes off the chart
g <- ggplot(testdata, aes(x = x, y = y))
g + geom_line() # shows outlier, scales the rest down, which may not be what we want
g + geom_line() + ylim(-3, 3) # skips the outlier (ggplot subsets the data)
g + geom_line() + coord_cartesian(ylim = c(-3, 3)) # outlier included, but off chart again (OK)
mpg$displ
str(mpg)

# Colors
library(RColorBrewer)
cols <- brewer.pal(3, "BuGn")
pal <- colorRampPalette(cols) # pal is function that generates colors for specified number
image(volcano, col = pal(20)) # here we will use 20 different colors
