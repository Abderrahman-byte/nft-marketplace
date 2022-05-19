import React, { useEffect, useState, useContext } from "react";
import CheckboxSettingsItem from "@Components/CheckBoxSettingsItem";
import { updateTokenSettings } from "@Utils/api";
import { AuthContext } from '@Context/AuthContext'
import LoadingCard from '@Components/LoadingCard'

import './styles.css'



const DetailsFixedBoxOwner =({id,isForSale, instantSale, price})=>{
    const {openModel, closeModel } = useContext(AuthContext)
    const [ISforSALL, setISFORSELL] = useState(isForSale)
    const [InstantSALe , setInstantSale] = useState(instantSale)
    const [Price, setPrice] =useState(price)
    const [done, setDone] = useState(false)

    const updateItem = async (data)=>{
        openModel(<LoadingCard />)
        const [result, error] = await updateTokenSettings(id, data)
        if(result && !error) setDone(true)
        closeModel()
       
    }

    
    const UpdateSattings=(e)=>{
       e.preventDefault();
  
      const data ={
          isForSale :ISforSALL
      }
      if (InstantSALe && Number.parseFloat(Price) > 0) {
        data.price = Number.parseFloat(Price)
      }

     data.instantSale = InstantSALe && Number.parseFloat(Price) > 0
     updateItem(data)
    }

    return(

        <div className="FixedBox" >
       
            <form className ="form-Update" onSubmit={UpdateSattings}>
             <CheckboxSettingsItem onChange={e => setISFORSELL(e.target.checked)}  defaultValue={ISforSALL} name='for-sell' subtitle='You’ll receive bids on this item' title='Put on sale' />
             <CheckboxSettingsItem  onChange={e => setInstantSale(e.target.checked)}   defaultValue={InstantSALe} name='instant-sell' subtitle='Instant sale' title='Put on Instante sale' />
             { 
             InstantSALe? 
                 (
             <div className='form-subdiv'> 
                 <input type='number' onChange={e=> setPrice(e.target.value)} value={Price} name='item-price' className='input-elt' min={0} step={0.001} />
             </div>
             )
                 :null
             } 
            
                 <button className='btn btn-blue'>
                    <span> Update </span>
                 </button>
                
            </form>
        </div>
    )
}
export default DetailsFixedBoxOwner;