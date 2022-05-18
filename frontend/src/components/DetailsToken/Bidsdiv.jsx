import React from "react";
import './styles.css'




const BidsDiv = ({from, price}) => {

    return (


        <div className="DetailsInfo ">

                <div className="frame-966">
                    <div className="avatar-1">
                        <img src={from.avatarUrl} alt="" />


                    </div>
                    <div className="infos-1">
                        <div className="frame-966">
                        <span className="type">
                            Bids 
                        </span>
                        <span className="price">
                        {price} RVN
                        </span>
                        <span className="type">
                           by 
                        </span>
                        </div>
                        <span className="FullName">
                           {from?.displayName || from?.username} 
                        </span>
                    </div>
                </div>
            
                <div className="horizontal-divider "></div>
        </div>
          

    )

}

export default BidsDiv;