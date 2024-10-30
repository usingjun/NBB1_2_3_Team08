package edu.leranermig.order.exception



class OrderTaskException(message: String, code: Int) : RuntimeException() {
    override val message: String? = null
    private val code = 0
}
