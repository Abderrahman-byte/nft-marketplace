import React, { useState, useEffect } from "react";
import DetailNavBar from "./DetailsNavBar";
import DetailsInfo from "./DetailsInfo";
import { convertRvnToUsd, formatMoney } from "@/utils/currency";
import DetailsFixedBox from "./DetailsFixedBox";
import DetailsBid from "./DetailsBids";
import DetailsHistory from "./DetailsHistory";
import DetailsFixedBoxOwner from "./DetailsFixedOwner";
import './styles.css'


const DetailsCard =({details, owner, creator, isOwner, onPriceUpdated, onAcceptedCallback, onPurchaseSuccess, onPlacedBid})=>{
    const [usdPrice, setUsdPrice] = useState(0)
    const [next, setnext] = useState("1")

    const updateUsdPrice = async () => {
        const price = await convertRvnToUsd(details?.price)

        if (price >= 0) setUsdPrice(price)
    }

    useEffect(() => {
        if (!details || !details.price) return
        updateUsdPrice()
    }, [details])
    return(
        <div className="DetailsCards">
            <div className="Bloc-1">
                <span className="title">{details.title}</span>
                <div className="Price-info">
                    <span className="RVN">  {formatMoney(details?.price || 0)} RVN</span>
                    <span className="USD">  ${formatMoney(usdPrice || 0)} </span>
                    <span className="Stok"> 10 in stock</span>
                </div>
            </div>
            <div className="Bloc-2">
            {details?.description}
            This NFT Card will give you Access to Special Airdrops.
            To learn more about check out unlockable
            </div>
            
            <div className="Bloc-3">
            <DetailNavBar setnext={setnext} />
            </div>
            <div className="details-container">
                {next === '1' && <DetailsInfo details={details} owner={owner || creator} creator={creator} />}
                {next === '3' && <DetailsHistory Id={details.id}/>}
                {next === '4'&& <DetailsBid onAcceptedCallback={onAcceptedCallback} Id={details.id} owner = {owner}/>}
            </div>

            {!isOwner ? (
                <DetailsFixedBox onPlacedBid={onPlacedBid} onPurchaseSuccess={onPurchaseSuccess} details = {details} owner ={owner} creator={creator}/>
            ) :(
                <DetailsFixedBoxOwner id={details.id} isForSale ={details?.isForSale } instantSale = {details?.instantSale} price ={details?.price} onPriceUpdated={onPriceUpdated} />
            )}
        </div>
    )
}

export default DetailsCard;
