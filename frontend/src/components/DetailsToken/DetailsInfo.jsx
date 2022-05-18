import React from "react";
import './styles.css'

const DetailsInfo = ({ owner, creator }) => {

    return (
        <div className="DetailsInfo ">
            <div className="frame-966">
                <div className="avatar-1">
                <img src={owner?.avatarUrl} alt="" />
                </div>
                <div className="infos-1">
                <span className="type">
                    Owner
                </span>
                <span className="FullName">
                {owner?.displayName || owner?.username}
                </span>
                </div>
            </div>
            
            <div className="horizontal-divider"></div>
            <div className="frame-966">
                    <div className="avatar-1">
                    <img src={creator?.avatarUrl} alt="" />
                </div>
                <div className="infos-1">
                <span className="type">
                Creator
                </span>
                <span className="FullName">
                {creator?.displayName || creator?.username}
                </span>
                </div>
            </div>
            <div className="horizontal-divider"></div>
        </div>
    )
}
export default DetailsInfo;