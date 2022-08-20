trait ByteEncoder[A] {
  def encode(a: A): Array[Byte]

}

object ByteEncoder {
  def apply[A](implicit ev: ByteEncoder[A]): ByteEncoder[A] = ev

  implicit object StringByteEncoder extends ByteEncoder[String] {
    override def encode(a: String): Array[Byte] = a.getBytes
  }

}

implicit object Rot3StringByteEncoder extends ByteEncoder[String] {
  override def encode(a: String): Array[Byte] =
    a.getBytes.map(b => (b + 3).toByte)
}

ByteEncoder[String].encode("hello")





