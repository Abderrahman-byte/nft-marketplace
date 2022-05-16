import React, { useState, useEffect } from "react";
import HeartIconSVG from '@/components/HeartIconSVG'
import { deleteLikeToken, postLikeToken } from '@/utils/api'
import {useNavigate} from 'react-router-dom'

import './styles.css'


const DetailsIcons =({ id, Like, account })=>{


    const [isLiked, setLiked] = useState(Like)
    const navigate = useNavigate();
  
    const likeBtnClicked = async (e) => {
      e.preventDefault()
      const done = isLiked ? await deleteLikeToken(id) : await postLikeToken(id)
      if (done) setLiked(!isLiked)
    }
  
    useEffect(() => {
      setLiked(Like)
    }, [Like])
  
    const Close =(e)=>{
       navigate(-1)
    }
  
    return (
      <div className="Icons">
        <div className="icon icon-close" onClick={Close}>
          <i className="close-icon" > </i>
        </div>
        <div className="icon">
          <i className="share2-icon"></i>
        </div>
        <div className="icon icon-heart ">
          {account ? (<button onClick={likeBtnClicked} className={`like-btn ${isLiked ? 'liked' : ''}`} >
            <HeartIconSVG className='heart' />
          </button>
          ) : null}
        </div>
        <div className="icon ">
          <i className="more2-icon"></i>
        </div>
      </div>
    )


}
export default DetailsIcons;