package com.tbook.rewardsphere.model

import com.fasterxml.jackson.annotation.JsonProperty

class ProofResponse(
        val pagination: Pagination,
        val ids: List<Id>,
)

class Pagination(
        val total: Long,
        val per: Long,
        val current: Long,
        val next: Long,
)

class Id(
        val persona: String,
        val avatar: String,
        @JsonProperty("last_arweave_id")
        val lastArweaveId: String,
        @JsonProperty("activated_at")
        val activatedAt: String,
        val proofs: List<Proof>,
)

data class Proof(
        val platform: String,
        val identity: String,
        @JsonProperty("alt_id")
        val altId: String,
        @JsonProperty("created_at")
        val createdAt: String,
        @JsonProperty("last_checked_at")
        val lastCheckedAt: String,
        @JsonProperty("is_valid")
        val isValid: Boolean,
        @JsonProperty("invalid_reason")
        val invalidReason: String,
)