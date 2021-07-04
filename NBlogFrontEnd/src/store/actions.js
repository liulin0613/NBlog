export default {
    aIncrement(context){
        setTimeout(()=>{
            // 仍然使用mutations修改数据
            // context.state.count++  x
            context.commit('increment')
        },1000)
    }
}