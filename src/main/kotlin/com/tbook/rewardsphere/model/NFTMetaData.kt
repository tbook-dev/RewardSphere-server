package com.tbook.rewardsphere.model

import com.fasterxml.jackson.annotation.JsonProperty

class NFTMetaData(val name: String,
                  @JsonProperty("created_by") val createdBy: String,
                  val description: String,
                  val image: String,
                  val attributes: List<Attribute>) {
}

class Attribute(@JsonProperty("trait_type") val traitType: String, val value: Any)