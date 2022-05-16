import React, {useState, useEffect} from "react";
import PurchaseNowBtn from "../PurchaseNowBtn";
import PlaceBidBtn from "../PlaceBidBtn";
import { convertRvnToUsd, formatMoney } from "@/utils/currency";
import './styles.css'


const DetailsFixedBox=({ details, owner })=>{

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
    const Content=(hb,ifs,is)=>{

        if(hb && ifs){
            return highestBid(details?.highestBid)
        }else if(!ifs && !is){

          return <span style={{fontfamily :"Inter", fontsize: "3em",color: "#777e90"}}> This token is not for sell !!</span>

        }else {
           return <span style={{fontfamily :"Inter", fontsize: "1em",color: "#777e90"}}> There's no bids yet. Be the first!</span>
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
export default DetailsFixedBox;