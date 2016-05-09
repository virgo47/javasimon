dsfile <- "household_power_consumption.txt"
if (!file.exists(dsfile)) {
    filename <- "power.zip"
    if (!file.exists(filename)) {
        url <- "https://d396qusza40orc.cloudfront.net/exdata%2Fdata%2Fhousehold_power_consumption.zip"
        download(url, filename)
    }
    unzip(filename)
}

filesubset<-grep("^[12]/2/2007|^Date", readLines(dsfile), value=T)
df <- read.table(textConnection(filesubset), sep = ";", header = T, na.strings = "?", stringsAsFactors = F)
df$DateTime <- paste(df$Date, df$Time)
df$DateTime <- strptime(df$DateTime, "%d/%m/%Y %H:%M:%S")

# plot1.R
png(filename = "plot1.png", width = 480, height = 480)
hist(df$Global_active_power, col="red",
     xlab = "Global Active Power (kilowatts)", main = "Global Active Power")
dev.off()

# plot2.R
png(filename = "plot2.png", width = 480, height = 480)
with(df, plot(DateTime, Global_active_power, type = "l",
              ylab = "Global Active Power (kilowatts)", xlab = ""))
dev.off()

# plot3.R
png(filename = "plot3.png", width = 480, height = 480)
with(df, {
     plot(DateTime, Sub_metering_1, type = "n",
              ylab = "Energy sub metering", xlab = "")
     lines(DateTime, Sub_metering_1)
     lines(DateTime, Sub_metering_2, col = "red")
     lines(DateTime, Sub_metering_3, col = "blue")
    })
legend("topright", lty = 1, col = c("black", "red", "blue"), legend = names(df)[grep("^Sub", names(df))])
dev.off()

# plot4.R
png(filename = "plot4.png", width = 480, height = 480)
par(mfcol = c(2, 2))
with(df, {
    plot(DateTime, Global_active_power, type = "l",
              ylab = "Global Active Power", xlab = "")

    plot(DateTime, Sub_metering_1, type = "n",
         ylab = "Energy sub metering", xlab = "")
    lines(DateTime, Sub_metering_1)
    lines(DateTime, Sub_metering_2, col = "red")
    lines(DateTime, Sub_metering_3, col = "blue")
    legend("topright", bty = "n", lty = 1, col = c("black", "red", "blue"), legend = names(df)[grep("^Sub", names(df))])

    plot(DateTime, Voltage, type = "l", xlab = "datetime")

    plot(DateTime, Global_reactive_power, type = "l", xlab = "datetime")
})
dev.off()

