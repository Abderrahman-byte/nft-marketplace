import React, {useState, useEffect} from "react";

import PurchaseNowBtn from "../PurchaseNowBtn";
import PlaceBidBtn from "../PlaceBidBtn";
import { convertRvnToUsd, formatMoney } from "@Utils/currency";

import './styles.css'


const DetailsFixedBox=({ details, owner, onPurchaseSuccess, onPlacedBid})=>{
    const [usdPrice, setUsdPrice] = useState(0)
    const serviceFee = 1.5

    const updateUsdPrice = async (priceBid) => {
        const price = await convertRvnToUsd(priceBid)
        if (price >= 0) setUsdPrice(price)
    }

    const highestBid = (highestbid) => {
        if(!highestbid || !highestbid.price) return
        
        updateUsdPrice(highestbid.price)
        return highestBidbloc(highestbid?.from.username, 
                            formatMoney(highestbid.price || 0), 
                            formatMoney(usdPrice || 0), 
                            highestbid?.from.avatarUrl)
    }

    const highestBidbloc = (username, priceRVN, priceUsd, avatar) => {
        return (
            <div className="highest-bid">
                 <div className="avatar-1">
                    <img src={avatar} alt="" />
                </div>
                <div className="frame-959">
                    <div className="row1">
                        <span className="highest">
                            Highest bid by
                        </span>
                        <span className="name">
                            {username}
                        </span>
                    </div>
                    <div className="row2">
                        <span className="RVN">
                            {priceRVN} RVN
                        </span>
                        <span className="USD">
                            ${priceUsd}
                        </span>
                    </div>
                    <div>

                    </div>
                </div>
               
            </div>
        )

    }
    
    const ButtonForSell=(isForSell, instantSell)=>{
        return (
            <>
                {instantSell ? (<PurchaseNowBtn onPurchaseSuccess={onPurchaseSuccess} tokenId={details?.id} />) : null}
                {isForSell? (<PlaceBidBtn successCallback={onPlacedBid} tokenId={details?.id} ownerId={owner?.id} />) : null}
            </>
        )
    }

    const Content=(hb,ifs,is)=>{

        if(hb && ifs){
            return highestBid(details?.highestBid)
        }else if(!ifs && !is){

          return <span style={{fontsize: "3em",color: "#777e90"}}> This token is not for sell !!</span>

        }else {
           return <span style={{fontsize: "1em",color: "#777e90"}}> There's no bids yet. Be the first!</span>
        }
    }

    return (

        <div className="FixedBox">
             { /**Highest bid */}
            {/*(details?.highestBid && details?.isForSale )? highestBid(details?.highestBid)
            : <span style={{fontfamily :"Inter", fontsize: "14px",color: "#777e90"}}> There's no bids yet. Be the first!</span>*/
            Content(details?.highestBid,details?.isForSale,details?.instantSale )}
            
            <div className="Fixed-buttons">
             {
                 ButtonForSell(details?.isForSale, details?.instantSale)
             }
            </div>

            <div className="fixed-footer">
                <span>
                    Service fee
                </span>
                <span>
                    {serviceFee}%
                </span>
                <span>
                    {formatMoney(details?.price * serviceFee / 100)} RVN
                </span>
                <span>
                    {usdPrice ? '$' + formatMoney(usdPrice * serviceFee) : ''}
                </span>
            </div>
        </div>
    )



}
export default DetailsFixedBox;