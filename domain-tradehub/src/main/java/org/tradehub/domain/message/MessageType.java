package org.tradehub.domain.message;

public enum MessageType {

    // Orders
    CREATE_ORDER_MSG_TYPE("order/MsgCreateOrder"),
    CANCEL_ORDER_MSG_TYPE("order/MsgCancelOrder"),
    CANCEL_ALL_MSG_TYPE("order/MsgCancelAll"),
    EDIT_ORDER_MSG_TYPE("order/MsgEditOrder"),
    CREATE_MARKET_MSG_TYPE("market/MsgCreateMarket"),
    UPDATE_MARKET_MSG_TYPE("market/MsgUpdateMarket"),
    INITIATE_SETTLEMENT_MSG_TYPE("broker/MsgInitiateSettlement"),
    SET_TRADING_FLAG_MSG_TYPE("order/MsgSetTradingFlag"),
    // Positions
    SET_LEVERAGE_MSG_TYPE("leverage/MsgSetLeverage"),
    EDIT_MARGIN_MSG_TYPE("position/MsgSetMargin"),
    // Tokens
    MINT_TOKEN_MSG_TYPE("coin/MsgMintToken"),
    CREATE_TOKEN_MSG_TYPE("coin/MsgCreateToken"),
    CREATE_WITHDRAWAL_TYPE("coin/MsgWithdraw"),
    SEND_TOKENS_TYPE("cosmos-sdk/MsgSend"),
    // Oracle
    CREATE_ORACLE_TYPE("oracle/MsgCreateOracle"),
    CREATE_VOTE_TYPE("oracle/MsgCreateVote"),
    // Staking
    CREATE_VALIDATOR_MSG_TYPE("cosmos-sdk/MsgCreateValidator"),
    DELEGATE_TOKENS_MSG_TYPE("cosmos-sdk/MsgDelegate"),
    BEGIN_UNBONDING_TOKENS_MSG_TYPE("cosmos-sdk/MsgUndelegate"),
    BEGIN_REDELEGATING_TOKENS_MSG_TYPE("cosmos-sdk/MsgBeginRedelegate"),
    WITHDRAW_DELEGATOR_REWARDS_MSG_TYPE("cosmos-sdk/MsgWithdrawDelegationReward"),
    // Accounts
    CREATE_SUB_ACCOUNT_MSG_TYPE("subaccount/MsgCreateSubAccountV1"),
    ACTIVATE_SUB_ACCOUNT_MSG_TYPE("subaccount/MsgActivateSubAccountV1"),
    // Profile
    UPDATE_PROFILE_MSG_TYPE("profile/MsgUpdateProfile"),
    // Gov
    SUBMIT_PROPOSAL_TYPE("cosmos-sdk/MsgSubmitProposal"),
    DEPOSIT_PROPOSAL_TYPE("cosmos-sdk/MsgDeposit"),
    VOTE_PROPOSAL_TYPE("cosmos-sdk/MsgVote"),
    // AMM
    ADD_LIQUIDITY_MSG_TYPE("liquiditypool/AddLiquidity"),
    REMOVE_LIQUIDITY_MSG_TYPE("liquiditypool/RemoveLiquidity"),
    CREATE_POOL_MSG_TYPE("liquiditypool/CreatePool"),
    CREATE_POOL_WITH_LIQUIDITY_MSG_TYPE("liquiditypool/CreatePoolWithLiquidity"),
    LINK_POOL_MSG_TYPE("liquiditypool/LinkPool"),
    UNLINK_POOL_MSG_TYPE("liquiditypool/UnlinkPool"),
    SET_REWARDS_WEIGHTS_MSG_TYPE("liquiditypool/SetRewardsWeights"),
    SET_REWARD_CURVE_MSG_TYPE("liquiditypool/SetRewardCurve"),
    SET_COMMITMENT_CURVE_MSG_TYPE("liquiditypool/SetCommitmentCurve"),
    STAKE_POOL_TOKEN_MSG_TYPE("liquiditypool/StakePoolToken"),
    UNSTAKE_POOL_TOKEN_MSG_TYPE("liquiditypool/UnstakePoolToken"),
    CLAIM_POOL_REWARDS_MSG_TYPE("liquiditypool/ClaimPoolRewards"),
    LINK_POOL_PROPOSAL_TYPE("liquiditypool/LinkPoolProposal"),
    SET_REWARD_CURVE_PROPOSAL_TYPE("liquiditypool/SetRewardCurveProposal"),
    SET_REWARDS_WEIGHT_PROPOSAL_TYPE("liquiditypool/SetRewardsWeightsProposal"),
    SET_COMMITMENT_CURVE_PROPOSAL_TYPE("liquiditypool/SetCommitmentCurveProposal"),
    CHANGE_SWAP_FEE_PROPOSAL_TYPE("liquiditypool/ChangeSwapFeeProposal"),
    CHANGE_NUM_QUOTES_PROPOSAL_TYPE("liquiditypool/ChangeNumQuotesProposal"),
    // CDP
    CREATE_VAULT_TYPE_MSG_TYPE("collateralizeddebtposition/CreateVaultType"),
    ADD_COLLATERAL_MSG_TYPE("collateralizeddebtposition/AddCollateral"),
    REMOVE_COLLATERAL_MSG_TYPE("collateralizeddebtposition/RemoveCollateral"),
    ADD_DEBT_MSG_TYPE("collateralizeddebtposition/AddDebt"),
    REMOVE_DEBT_MSG_TYPE("collateralizeddebtposition/RemoveDebt"),
    // Fee
    SET_MESSAGE_FEE_TYPE("fee/SetMsgFee"),
    SET_MESSAGE_FEE_PROPOSAL_TYPE("fee/SetMsgFeeProposal");

    public final String value;

    MessageType(String type) {
        this.value = type;
    }
}
