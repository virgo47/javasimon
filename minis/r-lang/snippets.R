# Downloading data
if (!file.exists("data")) {
    dir.create("data")
}
fileUrl <- "https://data.baltimorecity.gov/api/views/dz54-2aru/rows.csv?accessType=DOWNLOAD"
download.file(fileUrl, destfile = "./data/cameras.csv", method="curl")
dateDownloaded <- date()

cameraData <- read.table("./data/cameras.csv", sep = ",", header = TRUE)
# quote, na.strings, nrows (limits rows), skip (=offset)
# read.csv implies sep=",", header=TRUE
head(cameraData)

# excel
# before first use: install.packages('xlsx',repos='http://cran.r-project.org')
# this needs Java:
# install.packages('rJava',repos='http://cran.r-project.org')
# rJava uses "default" java depending on R runtime (32/64b), I just switched it to 32b in RStudio
library(xlsx)
read.xlsx("./data/cameras.xlsx", sheetIndex=1, colIndex = 2:3, rowIndex = 1:4)
# write.xlsx(...)

# XML
# install.packages('XML',repos='http://cran.r-project.org')
library(XML)
fileUrl <- "http://www.w3schools.com/xml/simple.xml"
doc <- xmlTreeParse(fileUrl, useInternal = TRUE)
rootNode <- xmlRoot(doc)
xmlName(rootNode)
names(rootNode)
rootNode[[1]]
# applying xmlValue function to rootNode
xmlSApply(rootNode, xmlValue)
# XPATH
xpathSApply(rootNode, "//name", xmlValue)

# HTML (also can use xpath)
doc <- htmlTreeParse(fileUrl, useInternal = TRUE)
scores <- xpathSApply(doc, "//li[@class='score']", xmlValue)

# JSON
# install.packages('jsonlite')
# install.packages('curl') # for fromJSON with URL
library(jsonlite)
jsonData <- fromJSON("https://api.github.com/users/virgo47")
jsonData
names(jsonData)
jsonData$login
# turn different dataset to JSON
myjson <- toJSON(iris, pretty = TRUE)
myjson

# data.table often faster/effecient alternative to data frames
#install.packages('data.table')
library(data.table)
# instead of:
DF = data.frame(x=rnorm(9),y=rep(c("a","b", "c"), each=3), z=rnorm(9))
head(DF,3)
# data table version:
DT = data.table(x=rnorm(9),y=rep(c("a","b", "c"), each=3), z=rnorm(9))
head(DT,3)
tables() # list of all data tables
# subsetting
DT[2,]
DT[DT$y=="a",]
DT[c(2,3)] # here it returns rows, not column subset (DIFFERENT from data frames!)
DT[,list(mean(x),sum(z))] # this applies functions
DT[,table(y)]
DT[2:5,x<-x+1] # returns vector of x values (no change to DT), for rows 2 through 5
DT[,x:=x+1] # this changes x values in DT object!


# DPLYR
package(dplyr)
cran <- tbl_df(mydf) # loads data into "data frame tbl" (from data frame, etc.)
select(cran, -(X:size)) # select columns (with - removing them) + changes order
filter(cran, r_version <= "3.0.2", country=="IN") # filter rows
filter(cran, country == "US" | country == "IN")

# Subsetting and Sorting
set.seed(13435)
X <- data.frame("var1"=sample(1:5), "var2"=sample(6:10), "var3"=sample(11:15))
X <- X[sample(1:5),] # reorder randomly
X$var2[c(1,3)] = NA # introduce 2 NA's in var2
X
X[,1] # subsetting by column, the same result as X[,"var1"] or X$var1
X[1:2, "var2"] # subsetting by rows and columns
X[(X$var1 <= 3 & X$var3 > 11),] # only rows matching condition (OR is |)
X[X$var2 > 8,] # does NOT remove NAs
X[which(X$var2 > 8),] # DOES remove NAs
# sorts the subset, but not the data frame
sort(X$var1)
sort(X$var2, decreasing=TRUE, na.last=TRUE) # without na.last NAs are missing
# ordering the data frame
X[order(X$var1, X$var3),]
# the same with plyr (not to confuse with dplyr?)
#install.packages('plyr')
library(plyr)
arrange(X, var1)
arrange(X, desc(var2), var3)
# add column to data frame
X$var4 <- rnorm(5)
Y <- cbind(X, rnorm(5)) # column bind (add column, in that order), rbind is for rows

# Summarizing
head(iris, n=3)
summary(iris) # stats and count for factors
str(iris) # variable types, some values/factor summary
quantile(iris$Sepal.Length, na.rm=TRUE)
table(iris$Species, useNA="ifany") # how many of what value
table(iris$Species, iris$Petal.Width) # 2D (and more) table, counts combinations
sum(is.na(iris$Sepal.Length)) # count of NAs (because TRUE ~ 1)
any(is.na(iris$Sepal.Length)) # TRUE/FALSE (FALSE here)
all(!is.na(iris$Sepal.Width)) # TRUE in this case (because of the not)
colSums(is.na(iris)) # works on numbers too, not just booleans
colSums(iris[,1:3]) # we had to skip 4th column, otherwise: Error in colSums(iris) : 'x' must be numeric
all(colSums(is.na(iris))==0) # TRUE means there are none NAs
table(iris$Sepal.Length %in% c(5.5, 5.0)) # table returns counts for TRUE and FALSE values in the boolean vector
iris[iris$Sepal.Length %in% c(5.5, 5.0),] # subset with Sepal.Lenght 5.5 or 5.0 (IN)
# cross tabs
data(UCBAdmissions) # loads dataset (introduces UCBAdmissions variable (promise))
DF = as.data.frame(UCBAdmissions)
# Create a contingency table (optionally a sparse matrix) from cross-classifying factors, 
# usually contained in a data frame, using a formula interface.
xtabs(Freq ~ Gender + Admit, data=DF) # Freq, Gender, Admit are all variables in DF
warpbreaks$replicate <- rep(1:9, len = 54)
xt <- xtabs(breaks ~., data=warpbreaks) # three dimensions (breaks + 3 more variables)
ftable(xt) # combines wool+tension into one dimension, hence 2 dim table is the result, more readable
fakeData = rnorm(1e5)
object.size(fakeData) # 800024 bytes
print(object.size(fakeData), units="Mb") # more readable: 0.5 Mb
