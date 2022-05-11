import React from "react";

import'./styles.css'
import PurchaseNowBtn from "../PurchaseNowBtn";


const FixedBox =({details, owner,creator})=>{
 


    return (
        <div className="fixed">
            <div className="highest-bid">
               
               <div className="frame-959">
                   <div className="row1">
                       <span className="highest">
                           Highest bid by 
                       </span>
                       <span className="name">
                              Kohaku Tora
                       </span>
                   </div>
                   <div className="row2">
                        <span className="RVN">
                          1.46 RVN
                        </span>
                        <span className="dolar">
                         $2,764.89
                        </span>
                   </div>
                   <div>

                   </div>
               </div>
               <div className="avatar">
                    
               </div>
            </div>

            <div className="buttons">

              <PurchaseNowBtn tokenId = {details?.id} />
              <button className="btn btn-white">
                  <label className="right">Place a bid </label>

              </button>
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