# Random clustered dataframe
set.seed(1234)
x <- rnorm(12, rep(1:3, each = 4), 0.2)
y <- rnorm(12, rep(c(1, 2, 1), each = 4), 0.2)
dataFrame <- data.frame(x, y)

# Hierarchical Clustering
plot(hclust(dist(dataFrame)))
# or with dplyr
library(dplyr)
hClustering <- dataFrame %>% dist %>% hclust
plot(hClustering)
# heatmaps
dataMatrix <- dataFrame %>% data.matrix
# image with dendrogram for rows/cols
heatmap(dataMatrix)

# K-Means Clustering
kmeansObj <- kmeans(dataFrame, centers = 3)
names(kmeansObj)
kmeansObj$cluster # [1] 3 3 3 3 1 1 1 1 2 2 2 2

# Heatmaps
set.seed(1234)
dataMatrix <- as.matrix(dataFrame)[sample(1:12),]
kmeansObj <- kmeans(dataMatrix, centers = 3)
par(mfrow = c(1, 2), mar = c(2, 4, 0.1, 0.1))
# transposed and reversed order, so it is oriented like when printed
image(t(dataMatrix)[, nrow(dataMatrix):1], yaxt = "n")
# order changed => clustered
image(t(dataMatrix)[, order(kmeansObj$cluster)], yaxt = "n")

# Dimension Reduction
set.seed(12345)
dataMatrix <- matrix(rnorm(400), nrow = 40)
# show the image
par(mar = rep(0.2, 4))
image(1:10, 1:40, t(dataMatrix)[, nrow(dataMatrix):1])
# or dentrogram
par(mar = rep(0.2, 4))
heatmap(dataMatrix)
# let's add some pattern to the random data
set.seed(678910)
for (i in 1:40) {
    coinFlip <- rbinom(1, size = 1, prob = 0.5)
    if (coinFlip) {
        dataMatrix[i, ] <- dataMatrix[i, ] + rep(c(0,3), each = 5)
    }
}
# now image/dendrogram shows some pattern, but lets see ordered matrix images
hh <- hclust(dist(dataMatrix))
dataMatrixOrdered <- dataMatrix[hh$order, ]
par(mfrow = c(1, 3), mar = c(4,4,2,2))
image(t(dataMatrixOrdered)[, nrow(dataMatrixOrdered):1])
plot(rowMeans(dataMatrixOrdered), 40:1, xlab = "Row Mean", ylab = "Row", pch = 19)
plot(colMeans(dataMatrixOrdered), xlab = "Column", ylab = "Column Mean", pch = 19)
# singular value decomposition
svd1 <- svd(scale(dataMatrixOrdered))
str(svd1)
