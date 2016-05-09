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
library(dplyr)
cran <- tbl_df(mydf) # loads data into "data frame tbl" (from data frame, etc.)
select(cran, -(X:size)) # select columns (with - removing them) + changes order
# "select" with standard data frame would be (using mydf instead of cran):
i <- match("X", names(mydf))
j <- match("size", names(mydf))
mydf[, -(i:j)]
# filter rows with dplyr
filter(cran, r_version <= "3.0.2", country=="IN")
filter(cran, country == "US" | country == "IN")
# other verbs:
# arrange - reorder rows
# rename - rename variables
# mutate - add new variables/columns or transform existing ones
# summarize/summarise - generate summary stats of variables
# print - does not print everything to console, looks better
# first argument is always data frame, returns new data frame - except for pipelines:
x <- tbl_df(iris) %>% rename(SepalLen = Sepal.Length) %>% 
    summarize(slMean = mean(SepalLen), swMean = mean(Sepal.Width))

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

# Sequences
seq(1, 10, by=2) # 1,3,5,7,9
seq(1, 10, length=3) # 1.0, 5.5, 10.0
seq(along = c(1,3,55,4)) # sequence of indexes, like 1:4 in this case

# Creating new variables (first 4 lines to load the data)
if (!file.exists("./data")) {dir.create("./data")}
fileUrl <- "https://data.baltimorecity.gov/api/views/k5ry-ef3g/rows.csv?accessType=DOWNLOAD"
download.file(fileUrl, destfile="./data/restaurants.csv",method="curl")
restData <- read.csv("./data/restaurants.csv")
# now the action
restData$nearMe = restData$neighborhood %in% c("Roland Park", "Homeland")
table(restData$nearMe) # summary counts of the new variable
restData$zipWrong = ifelse(restData$zipCode < 0, TRUE, FALSE)
table(restData$zipWrong, restData$zipCode < 0) # summary 2d table: new var vs condition 
# creating categorical variables
restData$zipGroups = cut(restData$zipCode, breaks=quantile(restData$zipCode))
table(restData$zipGroups) # category of zips vs percentiles
table(restData$zipGroups, restData$zipCode) # 2d table with all zips and the category (percentile)
# Easier cutting
# install.packages('Hmisc')
library(Hmisc)
restData$zipGroups = cut2(restData$zipCode, g=4)
table(restData$zipGroups) # should provide the same result (headers are formatter better though) 
# create factor variables
restData$zcf <- factor(restData$zipCode)
restData$zcf[1:10]
class(restData$zcf) # factor
# levels of factor variables
yesno <- sample(c("yes","no"), size=10, replace=TRUE)
yesnofac = factor(yesno, levels=c("yes", "no")) # yes will be first level (1)
relevel(yesnofac,ref="no") # returns new factor with different reference level
as.numeric(yesnofac) # 1 2 1 1... instead of yes no yes yes
# Common transformations
# abs(x), sqrt(x), ceiling/floor(x), round/signif(x, digits=n), cos/sin/log/log2/log10/exp(x)

# Reshaping data (to tidy data up)
library(reshape2)
head(mtcars) # mpg is mile per gallon
mtcars$carname <- rownames(mtcars)
# Melt: for all id values (combination) there will be lines with each measure.vars
carMelt <- melt(mtcars,id=c("carname", "gear", "cyl"), measure.vars=c("mpg", "hp"))
carMelt[carMelt$carname == 'Mazda RX4',] # two lines for one car, one with mpg, one with hp
# Casting (recasting)
# runs with warning Aggregation function missing: defaulting to length, OK in this case
cylData <- dcast(carMelt, cyl ~ variable) # gives count (length ^^ = how many variables) per cyl
cylData <- dcast(carMelt, cyl ~ variable, mean) # gives mean of variables (per cyl)
tapply(InsectSprays$count, InsectSprays$spray, sum) # "apply "sum" to "count", along the "spray" var
# following three steps do the same as tapply above
spIns <- split(InsectSprays$count, InsectSprays$spray)
sprCount <- lapply(spIns, sum)
unlist(sprCount)
# or last two lines can be replaced by:
sapply(spIns, sum)
# with plyr package
library(plyr)
ddply(InsectSprays, .(spray), summarize, sum=sum(count))
# see also functions:
# acast - casting multi-dim arrays
# arrange - faster reordering without order()
# mutate - adding new variables

# Grouping and more with DPLYR
by_package <- group_by(cran, package)
pack_sum <- summarize(by_package, count = n(), unique = n_distinct(ip_id),
                      countries = n_distinct(country), avg_bytes = mean(size))
q99cnt <- quantile(pack_sum$count, probs = 0.99) # 99% sample quantile (we want top 1% of popular packages)
top_counts <- filter(pack_sum, count > q99cnt)
# dplyr printing shows only first 10 rows, View shows all
top_counts_sorted <- arrange(top_counts, desc(count))
View(top_counts_sorted)
# Chaining example:
cran %>%
    select(ip_id, country, package, size) %>%
    mutate(size_mb = size / 2^20) %>%
    filter(size_mb <= 0.5) %>%
    arrange(desc(size_mb))

# Merging data (2 data frames)
mergedData = merge(reviews, solutions, by.x="solution_id", by.y="id", all=TRUE)
# columns names the same will be suffixed with .x and .y
# if by.x/y are not specified, it will "autoguess" by columns with the same name
# but when all the values don't match, it will multiply rows
join(df1, df2) # joins only by the same variables, but it's faster
join_all(...) # can join more than 2 data frames

# MySQL
#install.packages("RMySQL")
library(RMySQL)
ucscDb <- dbConnect(MySQL(), user="genome", host="genome-mysql.cse.ucsc.edu")
result <- dbGetQuery(ucscDb, "show databases;")
dbDisconnect(ucscDb) # very important to disconnect as soon as possible after getting data
hg19 <- dbConnect(MySQL(), user="genome", host="genome-mysql.cse.ucsc.edu", db = "hg19")
allTables <- dbListTables(hg19)
length(allTables)
allTables[1:5]
dbListFields(hg19, "affyU133Plus2") # lists column of the table
dbGetQuery(hg19, "select count(*) from affyU133Plus2")
affyData <- dbReadTable(hg19, "affyU133Plus2")
head(affyData)
query <- dbSendQuery(hg19, "select * from affyU133Plus2 where misMatches between 1 and 3")
affyMis <- fetch(query)
dim(affyMis) # 500 22
quantile(affyMis$misMatches)
affyMisSmall <- fetch(query, n=10); dbClearResult(query);
dim(affyMisSmall) # 10 22
dbDisconnect(hg19) # !!! of course close it :-)

# reading from web
con = url("http://...")
htmlCode = readLines(con)
close(con) # don't forget to close the connection!
# httr package
#install.packages("httr")
library(httr)
html2 = GET(url)
content2 = content(html2, as="text")
parsedHtml = htmlParse(content2, asText=TRUE) # works like htmlTreeParse(url, useInternalNodes=T)
# authentication
pg2 = GET(url, authenticate("user", "password"))
# handles
google = handle("http://google.com")
pg1 = GET(handle=google, path="/") # if this authenticates, handle will remember it