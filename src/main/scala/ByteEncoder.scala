trait ByteEncoder[A] {
  def encode(a: A): Array[Byte]

}

object ByteEncoder {
  def apply[A](implicit ev: ByteEncoder[A]): ByteEncoder[A] = ev

}
implicit object StringByteEncoder extends ByteEncoder[String] {
  override def encode(a: String): Array[Byte] = a.getBytes
}

implicit def optionEncoder[A](implicit encA: ByteEncoder[A]): ByteEncoder[Option[A]] = new ByteEncoder[Option[A]] {
  override def encode(a: Option[A]): Array[Byte] = {
    a match {
      case Some(value) => encA.encode(value)
      case None => Array[Byte]()
    }
  }
}


ByteEncoder[String].encode("hello")





