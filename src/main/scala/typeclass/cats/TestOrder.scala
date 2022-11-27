package typeclass.cats

import cats._
import cats.implicits._
import typeclass.cats.TestEq.Account

object TestOrder extends App {

  case class Account(id: Long, number: String, balance: Double, owner: String)

  object Account {
    implicit def orderById(implicit orderLong: Order[Long]): Order[Account] = Order.from((a1, a2) => orderLong.compare(a1.id, a2.id))

    object instances {
      implicit var orderByNumber: Order[Account] = Order.by(account => account.number)
      implicit val orderByBalance: Order[Account] = Order.by(account => account.balance)
    }
  }

  def sort[A](list: List[A])(implicit orderA: Order[A]) = list.sorted(orderA.toOrdering)

  val account1 = Account(1, "234-456", 1000, "Lucy")
  val account2 = Account(2, "234-456", 1000, "Lily")
//  println(sort[Account](List(account1, account2)))
  implicit val orderByIdDesc: Order[Account] = Order.reverse(Account.orderById)
  println(sort[Account](List(account1, account2)))


}
