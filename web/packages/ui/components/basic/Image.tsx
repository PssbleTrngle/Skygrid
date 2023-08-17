import { FC } from "react";
import { ImgFactoryProps, useElementFactory } from "../../context/elements";

const Image: FC<ImgFactoryProps> = (props) => {
  const { createImg } = useElementFactory();
  return createImg(props);
};

export default Image;
