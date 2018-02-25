import java.time.temporal.TemporalAdjusters._
import java.time.{LocalDateTime, LocalTime, ZoneOffset, ZonedDateTime}
object Java8DateAndTime extends App {

  val timePoint = LocalDateTime.now()

  val foo = timePoint.`with`(lastDayOfMonth())


  // Using value classes as adjusters


  println("timePoint: " + timePoint )
  println("foo: " + foo )

  println("timePoint.`with`(LocalTime.now()): " + timePoint.`with`(LocalTime.now()))

  //  ZonedDateTime is a date and time with a fully qualified time zone (see Listing 8).
  // This can resolve an offset at any point in time.
  // The rule of thumb is that if you want to represent a date and time without relying on the context of a specific server, you should use ZonedDateTime.

  ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]")

  //OffsetDateTime is a date and time with a resolved offset.
  // This is useful for serializing data into a database and also should be used as the serialization format for logging time stamps if you have servers in different time zones.

  import java.time.OffsetTime

  val time = OffsetTime.now
  val offset = ZoneOffset.ofHours(2)
  // changes offset, while keeping the same point on the timeline
  val sameTimeDifferentOffset = time.withOffsetSameInstant(offset)
  // changes the offset, and updates the point on the timeline
  val changeTimeWithNewOffset = time.withOffsetSameLocal(offset)
  // Can also create new object with altered fields as before
  changeTimeWithNewOffset.withHour(3).plusSeconds(2)
}