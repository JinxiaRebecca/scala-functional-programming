import cats._
import cats.implicits._

case class Account(id: Long, number: String, balance: Double, owner: String)

object Account {
  implicit def orderById(implicit orderLong: Order[Long]): Order[Account] = Order.from((a1, a2) => orderLong.compare(a1.id, a2.id))

  object instances {
    implicit val orderByNumber: Order[Account] = Order.by(account => account.number)

    implicit def orderByBalance(implicit orderDouble: Order[Double]): Order[Account] = Order.by(account => account.balance)
  }
}

def sort[A](list: List[A])(implicit orderA: Order[A]) = {
  list.sorted(orderA.toOrdering)
}

val account1 = Account(1, "442-21", 3500, "Lucy")
val account2 = Account(2, "442-21", 3000, "Lily")

account1 compare account2

sort[Account](List(account1, account2))

import Account.instances.orderByBalance
sort(List(account1, account2))