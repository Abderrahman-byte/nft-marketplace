import { DEFAULT_HEADERS, getRequest } from "./http"

const FALLBACK_PRICE = 0.03

export const convertRvnToUsd = async (amount) => {
    const response = await fetch('https://api.binance.com/api/v3/ticker/price?symbol=RVNUSDT')
    const data = await response.json()

    console.log(data)

    const price = Number.parseFloat(data?.price || FALLBACK_PRICE)

    return price * amount
}

export const formatMoney = (amount) => {
    return amount.toLocaleString('en-US')
}