package com.tbook.rewardsphere.model

import com.fasterxml.jackson.annotation.JsonProperty

class NFTMetaData(val name: String,
                  @JsonProperty("created_by") val createdBy: String,
                  val description: String,
                  val image: String) {
}