package taxipark

import kotlin.math.floor

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
    val workingDrivers = trips.map { it.driver }.distinct()
    return allDrivers.filter { !workingDrivers.contains(it) }.toSet()
}

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        if (minTrips == 0)
            allPassengers
        else
            trips.flatMap { it.passengers }.groupBy { it }.filter { it.value.count() >= minTrips }.map { it.key }.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        trips.filter { it.driver == driver }.flatMap { it.passengers }.groupBy { it }.filter { it.value.count() > 1 }.map { it.key }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        trips.flatMap { t -> t.passengers.map { p -> Pair(p, t.discount) } }
                .groupBy(keySelector = { it.first }, valueTransform = { it.second })
                .mapValues { it.value.partition { d -> d != null } }
                .filter { it.value.first.count() > it.value.second.count() }
                .map { it.key }
                .toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val mostFrequentRange = trips.map { it.duration / 10 }.groupingBy { it }.eachCount().maxBy { it.value }?.key
            ?: return null
    return IntRange(10 * mostFrequentRange, 10 * mostFrequentRange + 9)
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.count() < 1) return false
    val p20Drivers = floor(allDrivers.count() * 0.2).toInt()
    val totalIncome = trips.map { it.cost }.sum()
    val p80Income = totalIncome * 0.8
    val p20DriversIncome = trips.groupingBy { it.driver }.aggregate { _, accumulator: Double?, element, first -> if (first) element.cost else accumulator!!.plus(element.cost) }
            .filter { it.value != null }
            .mapValues { it.value!! }
            .entries.sortedBy { it.value }.reversed().take(p20Drivers).map { it.value }.sum()

    return p20DriversIncome >= p80Income
}