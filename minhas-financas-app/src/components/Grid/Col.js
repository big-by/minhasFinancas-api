import React from 'react'

export const Col6 = (props) => {
    return(
        <div className="col-md-6 offset-md-3">
            {props.children}
        </div>
    )
}

export const Col12 = (props) => {
    return(
        <div className="col-lg-12">
            {props.children}
        </div>
    )
}
