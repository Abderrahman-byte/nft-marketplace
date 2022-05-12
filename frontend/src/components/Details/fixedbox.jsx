import React, { useEffect, useState } from "react";

import './styles.css'
import PurchaseNowBtn from "../PurchaseNowBtn";
import PlaceBidBtn from "../PlaceBidBtn";
import { convertRvnToUsd, formatMoney } from "@/utils/currency";

const FixedBox = ({ details, owner }) => {
    const [usdPrice, setUsdPrice] = useState(0)

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
                        <span className="dolar">
                            ${priceUsd}
                        </span>
                    </div>
                    <div>

                    </div>
                </div>
                <div className="avatar">
                    <img src={avatar} alt="" />
                </div>
            </div>
        )

    }
    
    const ButtonForSell=(isForSell, instantSell)=>{
        if(isForSell && instantSell)
         {
             return(
                <>
                <PurchaseNowBtn tokenId={details?.id} />
                <PlaceBidBtn tokenId={details?.id} ownerId={owner?.id} />
                </>
                )
         }
         else if (isForSell && !instantSell) {
             return(
                 <>
                  <PlaceBidBtn tokenId={details?.id} ownerId={owner?.id} />
                 </>
             )
             
         }else if(!isForSell && instantSell){
             return(
                <>
                <PurchaseNowBtn tokenId={details?.id} />
                </>
             )
         }
    }
    return (

        <div className="fixed">
             { /**Highest bid */}
            {highestBid(details?.highestBid)}
            {console.log(details)}
            
            <div className="buttons">
             {
                 ButtonForSell(details?.isForSale, details?.instantSale)
             }
                {/*<PurchaseNowBtn tokenId={details?.id} />
                <PlaceBidBtn tokenId={details?.id} ownerId={owner?.id} />*/}
            </div>

            <div className="fixed-footer">
                <span>
                    Service free
                </span>
                <span>
                    1.5%
                </span>
                <span>
                    2.563 RVN
                </span>
                <span>
                    $4,540.62
                </span>
            </div>
        </div>
    )


}

export default FixedBox;