type Polymorphic<TType extends string = string> = {
  __typename?: TType;
};

export type Typename<TObject extends Polymorphic> = Extract<
  TObject["__typename"],
  string
>;

export type Mapper<TObject extends Polymorphic, TOut> = {
  [K in Typename<TObject>]?: (resolved: ResolvedPolymorph<TObject, K>) => TOut;
};

type ResolvedPolymorph<TObject, TKey extends string> = Extract<
  TObject,
  Polymorphic<TKey>
>;

export function forPolymorph<TObject extends Polymorphic, TOut>(
  value: TObject,
  mapper: Mapper<TObject, TOut>
): TOut | undefined {
  return undefined;
}
