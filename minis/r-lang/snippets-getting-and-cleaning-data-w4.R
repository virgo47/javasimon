# Editing Text Variables
fileUrl <- "https://data.baltimorecity.gov/api/views/dz54-2aru/rows.csv?accessType=DOWNLOAD"
filename <- "data/cameras.csv"
download.file(fileUrl, destfile=filename, method="curl")
cameraData <- read.csv("data/cameras.csv")
dim(cameraData)
names(cameraData)
tolower(names(cameraData))
splitNames <- strsplit(names(cameraData), "\\.") # list where [[6]] is vector of 2
mylist <- list(letters=c("A", "b", "c"), numbers=1:3, matrix(1:25, ncol=5))
mylist[[1]]
sapply(splitNames, function(x){x[1]})
sub("_", "", "x_y_z") # xy_z, replaces first, gsub replaces all (global)
grep("Alameda", cameraData$intersection) # which elements of the vector match
grep("Alameda", cameraData$intersection, value = T) # returns matching vectors
alameda <- grepl("Alameda", cameraData$intersection) # boolean vector with TRUE where matches
cameraData[alameda,] # subsetting only to intersections with Alameda

library(stringr)
nchar("Virgo47") # string length
substr("Whatever", 2,5) # substring from-to, included, counted from 1
paste("Something", "else", sep = "-") # concat, by default with separator " "
paste0("Super","Trouper") # like sep = ""
str_trim(" asdf   ") # like Java's trim

# Regular expressions
grep("a\\.b", "asdfa.b")


# working with Dates
date() # (now) "Thu Oct 29 22:35:22 2015", class: character
d2 <- Sys.Date() # just date, class: Date
format(d2, "%a %b %d") # "št okt 29"
d3 <- as.Date("1jan1960", "%d%b%Y") # returns Date
d2 - d3 # Time difference of 20390 days, class: difftime
# install.packages("lubridate")
library(lubridate)
ymd("20140108") # class: POSIXct
ymd_hms("2011-08-03T10:15:03") # class: POSIXct
wday(d3) # numeric
wday(d3, label=TRUE) # ordered factor (6 = Fri)

# tidyr
# gather - moves values from column names to the content of dataframe
# separate - separates two variables mixed in one column
# spread - spreads key-value pair across multiple columns (more rows -> one row)
#  grade male_1 female_1 male_2 female_2
#1     A      3        4      3        4
#2     B      6        4      3        5
students2 %>%
    gather(sex_class, count, -grade ) %>%
    separate(sex_class , c("sex", "class")) %>%
    print
#   grade    sex class count
#1      A   male     1     3
#2      B   male     1     6
#...
#6      A female     1     4
#7      B female     1     4
#...

#> students3
#    name    test class1 class2 class3 class4 class5
#1  Sally midterm      A   <NA>      B   <NA>   <NA>
#2  Sally   final      C   <NA>      C   <NA>   <NA>
#3   Jeff midterm   <NA>      D   <NA>      A   <NA>
#4   Jeff   final   <NA>      E   <NA>      C   <NA>
#5  Roger midterm   <NA>      C   <NA>   <NA>      B
#...
students3 %>%
    gather(class, grade, class1:class5, na.rm = TRUE) %>%
    spread(test, grade) %>%
    mutate(class = extract_numeric(class)) %>%
    print
#    name class final midterm
#1  Brian     1     B       B
#2  Brian     5     C       A
#3   Jeff     2     E       D
#...

# MULTIPLE observational units in the same table (id, name, sex - vs the rest)
#> students4
#    id  name sex class midterm final
#1  168 Brian   F     1       B     B
#2  168 Brian   F     5       A     C
#3  588 Sally   M     1       A     C
student_info <- students4 %>%
    select(id, name, sex) %>%
    unique() %>%
    print

# second table contains the class results
gradebook <- students4 %>%
    select(id, class, midterm, final) %>%
    print

# SINGLE observational unit in MULTIPLE tables
#> passed
#   name class final
#1 Brian     1     B
#2 Roger     2     A
#... dataset "failed" is similar with different names and grades
passed <- mutate(passed, status="passed")
failed <- mutate(failed, status="failed")
bind_rows(passed, failed)
