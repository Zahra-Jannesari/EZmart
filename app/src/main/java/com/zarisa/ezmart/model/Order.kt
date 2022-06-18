package com.zarisa.ezmart.model

data class Order (val id:Int,val discount_total:String,val total:String,val line_items:List<OrderItem>)