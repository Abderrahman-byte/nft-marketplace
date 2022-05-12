import React, { useState, useEffect } from "react";

import './styles.css'
import DetailNavbar from "./navbar";
import Info from "./info";
import { convertRvnToUsd, formatMoney } from "@/utils/currency";
import Bidscard from "./bids";
/*Stock ??*/
const Detailscard = ({ details, owner, creator }) => {
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

    return (
        <div className="frame-970">
            <div className="title">
                <span className="The-amazing-art">
                    {details.title}
                </span>
                <div className= 'frame-952'>
                <span className= 'price-rvn'>
                    {formatMoney(details?.price || 0)} RVN
                </span>

                <span className="price-d">
                    ${formatMoney(usdPrice || 0)}
                </span>

                <span className="stok">10 in stock</span>
                </div>
            </div>
            <div className="title-2">
                This NFT Card will give you Access to Special Airdrops.
                To learn more about check out unlockable
            </div>
            <div className="content">
                <DetailNavbar setnext={setnext} />
                {next === '1' && <Info details={details} owner={owner} creator={creator} />}
                {next === '4'&& <Bidscard details={details}/>}
            </div>
        </div>
    )
}
export default Detailscard;