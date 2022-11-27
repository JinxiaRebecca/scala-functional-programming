package typeclass.cats

import cats._
import cats.implicits._

object TestEq extends App {

  case class Account(id: Long, number: String, balance: Double, owner: String)

  object Account {
    implicit val universalEq: Eq[Account] = Eq.fromUniversalEquals //==

    object Instances {
      implicit def byIdEq(implicit eqLong: Eq[Long]): Eq[Account] = Eq.instance[Account]((a1, a2) => eqLong.eqv(a1.id, a2.id))
      implicit def byIdEq2(implicit eqLong: Eq[Long]): Eq[Account] = Eq.by(_.id)
      implicit def byNumber(implicit eqString: Eq[String]):Eq[Account] = Eq.by(_.number)

    }
  }

  val account1 = Account(1, "234-456", 1000, "Lucy")
  val account2 = Account(2, "234-456", 1000, "Lily")

  println(Eq[Account].eqv(account1, account2)) // universalEq
  println(Account.Instances.byIdEq2.eqv(account1, account2))
  println(Account.Instances.byNumber.eqv(account1, account2))

  import Account.Instances.byNumber
  println(account1==account2)

  implicit val eqToUse = Account.Instances.byIdEq2
  println(account1==account2)
}
