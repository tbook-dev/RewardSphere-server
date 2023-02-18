package com.tbook.rewardsphere.model

import com.fasterxml.jackson.annotation.JsonProperty

data class GoPlusMaliciousResponse(
        val code: Long,
        val message: String,
        val result: Result,
)

data class Result(
        val cybercrime: String,
        @JsonProperty("money_laundering")
        val moneyLaundering: String,
        @JsonProperty("number_of_malicious_contracts_created")
        val numberOfMaliciousContractsCreated: String,
        @JsonProperty("financial_crime")
        val financialCrime: String,
        @JsonProperty("darkweb_transactions")
        val darkwebTransactions: String,
        @JsonProperty("phishing_activities")
        val phishingActivities: String,
        @JsonProperty("fake_kyc")
        val fakeKyc: String,
        @JsonProperty("blacklist_doubt")
        val blacklistDoubt: String,
        @JsonProperty("data_source")
        val dataSource: String,
        @JsonProperty("stealing_attack")
        val stealingAttack: String,
        @JsonProperty("blackmail_activities")
        val blackmailActivities: String,
        val sanctioned: String,
        @JsonProperty("malicious_mining_activities")
        val maliciousMiningActivities: String,
        val mixer: String,
        @JsonProperty("honeypot_related_address")
        val honeypotRelatedAddress: String,
)
