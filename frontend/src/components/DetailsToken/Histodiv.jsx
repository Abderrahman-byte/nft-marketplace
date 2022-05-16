import React from "react";
import './styles.css'




const HistoDiv = ({from, to}) => {

    return (


        <div className="DetailsInfo ">

                <div className="frame-966">
                    <div className="avatar-1">
                        <img src={to.avatarUrl} alt="" />
                    </div>
                    <div className="infos-1">
                       <div className="frame-966">
                        <span className="type">
                           Transfered
                        </span>
                        <span className="type">
                            to 
                        </span>
                        <span className="price">
                        {to.username}
                        </span>
                        </div>
                        <span className="FullName">
                          by {from.username}
                        </span>
                    </div>
                </div>
            
                <div className="horizontal-divider "></div>
        </div>
          

    )

}

export default HistoDiv;