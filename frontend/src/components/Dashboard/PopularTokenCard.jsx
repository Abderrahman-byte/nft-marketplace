import React from "react";

import './styles.css'


const PopularTokenCard = () => {

    return (
        <div className="PopularTokenCard">
            <div className="details-1">
                <span >
                Silk Road #44
                </span>
                <div className="creator--price">
                   <div className="creator">
                       <div className="avatar">
                    
                       </div>
                       <div className="type-full">
                       <span className="type">
                            Owner
                        </span>
                        <span className="FullName">
                           Full Name
                        </span>
                        </div>
                   </div>
                   <div className="collection">
                   <div className="avatar">
                    
                    </div>
                    <div className="type-full">
                    <span className="type">
                         Owner
                     </span>
                     <span className="FullName">
                        Full Name
                     </span>
                     </div>
                   </div>
                </div>
            </div>
            <div className="details-2">
            </div>
            <div className="details-3">
            </div>
        </div>
    )

}

export default PopularTokenCard;