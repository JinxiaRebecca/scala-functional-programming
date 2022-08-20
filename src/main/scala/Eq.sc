import cats._
import cats.implicits._

case class Account(id: Long, number: String, balance: Double, owner: String)

object Account {
  //每个属性值相等时则相等
  implicit val universalEq: Eq[Account] = Eq.fromUniversalEquals

  object  instances {
    implicit def byIdEq(implicit eqLong: Eq[Long]): Eq[Account] = Eq.instance[Account]((a1, a2) => eqLong.eqv(a1.id, a2.id))

    implicit def byNumber(implicit eqString: Eq[String]): Eq[Account] = Eq.by(_.number)
  }
}
val account1 = Account(1, "123-456", 1000, "Lucy")
val account2 = Account(1, "123-456", 1045, "Lucy")
Eq[Account].eqv(account1, account2) //false

Account.instances.byIdEq.eqv(account1, account2)  //true
Account.instances.byNumber.eqv(account1, account2) //true

//import Account.instances.byNumber
//account1 === account2 // true

implicit val eqToUse = Account.instances.byNumber
account1 === account2 //true