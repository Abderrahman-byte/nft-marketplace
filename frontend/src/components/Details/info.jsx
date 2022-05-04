import React from "react";
import './styles.css'





const Info = ({ details, owner, creator }) => {



    return (
        <div className="Info ">
            <div className="frame-967">
                <div className="frame-966">
                    <div className="avatar">

                        <img src={owner.avatarUrl} alt="" />
                        <div className="box">
                        </div>

                    </div>
                    <div className="frame-965">
                        <span className="type">
                            Owner
                        </span>
                        <span className="FullName">
                            {owner.username}
                        </span>
                    </div>
                </div>
            </div>
            <div className="devider"> </div>
            <div className="frame-967">
                <div className="frame-966">
                    <div className="avatar">

                        <img src={creator.avatarUrl} alt="" />
                        <div className="box">
                        </div>

                    </div>
                    <div className="frame-965">
                        <span className="type">
                            Creator
                        </span>
                        <span className="FullName">
                            {creator.username}
                        </span>
                    </div>
                </div>
            </div>
            <div className="devider"> </div>
        </div>
    )
}
export default Info;