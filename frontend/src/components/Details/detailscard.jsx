import React, {useState, useEffect} from "react";

import './styles.css'
import DetailNavbar from "./navbar";
import Info from "./info";
const Detailscard = ({details, owner, creator}) => {

    const [next, setnext]=useState("1")
    return (
        <div className="frame-970">
            <div className="title">
                <span className="The-amazing-art">
                   {details.title}
                 
                </span>
                <div className="frame-952">
                    <div className="price-rvn">
                        <span>
                            2,500 RVN
                        </span>

                    </div>
                    <div className="price-d">
                        <span>
                        ${details.price}
                        </span>

                    </div>
                    <span className="stok">
                    10 in stock
                    </span>
                </div>
            </div>
            <div className="title-2">
            This NFT Card will give you Access to Special Airdrops.
             To learn more about check out unlockable
            </div>
            <div className="content">
                 <DetailNavbar next={next} setnext={setnext}/>        
               {next ==='1'  && <Info details={details} owner={owner} creator={creator}/>}
            </div>

        </div>

    )

}
export default Detailscard;