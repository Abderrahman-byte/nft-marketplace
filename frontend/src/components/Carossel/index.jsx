import React, { useEffect, useRef, useState } from 'react'

import './styles.css'

// TODO : fix gap

const Carossel = ({ title, children, childrenWidthPercentage  = 30, gapPersentage = 5, setChildrenWidth, setGap }) => {
    const [position, setPosition] = useState(0)
    const rowRef = useRef()
    const containerRef = useRef()

    const isNextEnabled = () => {
        if (!rowRef.current) return false
        const rowWidth = parseFloat(getComputedStyle(rowRef.current).width)
        const containerWidth = containerRef.current.scrollWidth
        const scrollTimes = containerWidth / rowWidth

        if ((position + rowWidth) / rowWidth >= scrollTimes) return false

        return true
    }

    const isPrevEnabled = () => {
        if (!rowRef.current) return false
        const rowWidth = parseFloat(getComputedStyle(rowRef.current).width)
        const newPosition = position - rowWidth

        return newPosition >= 0
    }

    
    const nextClicked = () => {
        const rowWidth = parseFloat(getComputedStyle(rowRef.current).width)
        
        if (!isNextEnabled()) return

        setPosition(position + rowWidth)
    }

    const prevClicked = () => {
        const rowWidth = parseFloat(getComputedStyle(rowRef.current).width)
        const newPosition = position - rowWidth

        if (newPosition < 0) return

        setPosition(newPosition - rowWidth < 0 ? 0 : newPosition)
    }

    useEffect(() => {
        if (!rowRef.current || typeof setChildrenWidth !== 'function') return

        const rowWidth = parseFloat(getComputedStyle(rowRef.current).width)

        setChildrenWidth(rowWidth / 100 * childrenWidthPercentage)
        setGap(rowWidth / 100 * gapPersentage)
    },  [rowRef.current, childrenWidthPercentage, gapPersentage])

    return (
        <div className='Carossel'>
            <div className='Carossel-header'>
                <h3>{title}</h3>
                <div className='controls'>
                    <button disabled={!isPrevEnabled()} onClick={isPrevEnabled() ? prevClicked : null} className='btn prev-btn'>
                        <i className='big-arrow-left-icon'></i>
                    </button>
                    <button disabled={!isNextEnabled()} onClick={isNextEnabled() ? nextClicked : null} className='btn next-btn'>
                        <i className='big-arrow-right-icon'></i>
                    </button>
                </div>
            </div>

            <div className='row-container' ref={rowRef}>
                <div ref={containerRef} style={{ transform: `translate3d(-${position}px, 0px, 0px)` }} className='row'>
                    {children}
                </div>
            </div>
        </div>
    )
}

export default Carossel