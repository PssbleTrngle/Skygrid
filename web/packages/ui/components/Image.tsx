import styled from "styled-components";

const Image = styled.img<{ $size?: number; $objectFit?: string }>`
  ${(p) => p.$objectFit && `object-fit: ${p.$objectFit}`};
  ${(p) => p.$size && `height: ${p.$size}px`};
  ${(p) => p.$size && `width: ${p.$size}px`};
`;

export default Image;
