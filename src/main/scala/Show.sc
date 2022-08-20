import cats._
import cats.implicits._

case class Account(id: Long, number: String, balance: Double, owner: String)

object Account {
  implicit val toStringShow: Show[Account] = Show.fromToString

  object Instances {
    implicit val byOwnerAndBanlance: Show[Account] = Show.show { account =>
      s"${account.owner} - ${account.balance}"
    }

    implicit val prettyByOwener: Show[Account] = Show.show{ account =>
      s"This account belongs to ${account.owner}"
    }
  }
}

val account = Account(1, "123-45", 1200, "Lucy")

Account.toStringShow.show(account)
account.show
Account.Instances.byOwnerAndBanlance.show(account)
Account.Instances.prettyByOwener.show(account)