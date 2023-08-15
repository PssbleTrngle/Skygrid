/* eslint-disable */
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
export type MakeEmpty<T extends { [key: string]: unknown }, K extends keyof T> = { [_ in K]?: never };
export type Incremental<T> = T | { [P in keyof T]?: P extends ' $fragmentName' | '__typename' ? T[P] : never };
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: { input: string; output: string; }
  String: { input: string; output: string; }
  Boolean: { input: boolean; output: boolean; }
  Int: { input: number; output: number; }
  Float: { input: number; output: number; }
};

export type BaseBlockProvider = {
  except?: Maybe<Array<Filter>>;
  extra?: Maybe<Scalars['Boolean']['output']>;
  extras?: Maybe<Array<Extra>>;
  icon?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  weight: Scalars['Float']['output'];
};

export type BaseExtra = {
  children: Array<BlockProvider>;
  probability: Scalars['Float']['output'];
};

export type Block = BaseBlockProvider & Named & WeightedEntry & {
  __typename?: 'Block';
  except?: Maybe<Array<Filter>>;
  extra?: Maybe<Scalars['Boolean']['output']>;
  extras?: Maybe<Array<Extra>>;
  icon?: Maybe<Scalars['String']['output']>;
  id: Scalars['String']['output'];
  mod?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  weight: Scalars['Float']['output'];
};

export type BlockList = BaseBlockProvider & WeightedEntry & {
  __typename?: 'BlockList';
  children: Array<BlockProvider>;
  except?: Maybe<Array<Filter>>;
  extra?: Maybe<Scalars['Boolean']['output']>;
  extras?: Maybe<Array<Extra>>;
  icon?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  weight: Scalars['Float']['output'];
};

export type BlockProvider = Block | BlockList | Fallback | Reference | Tag;

export type CardinalExtra = BaseExtra & {
  __typename?: 'CardinalExtra';
  children: Array<BlockProvider>;
  offset: Scalars['Int']['output'];
  probability: Scalars['Float']['output'];
};

export type DimensionConfig = {
  __typename?: 'DimensionConfig';
  blocks: Array<BlockProvider>;
  id: Scalars['String']['output'];
  loot: Array<LootTable>;
};

export enum Direction {
  Down = 'DOWN',
  East = 'EAST',
  North = 'NORTH',
  South = 'SOUTH',
  Up = 'UP',
  West = 'WEST'
}

export type Extra = CardinalExtra | OffsetExtra | SideExtra;

export type Fallback = BaseBlockProvider & WeightedEntry & {
  __typename?: 'Fallback';
  children: Array<BlockProvider>;
  except?: Maybe<Array<Filter>>;
  extra?: Maybe<Scalars['Boolean']['output']>;
  extras?: Maybe<Array<Extra>>;
  icon?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  weight: Scalars['Float']['output'];
};

export type Filter = ModFilter | NameFilter | TagFilter;

export type LootTable = WeightedEntry & {
  __typename?: 'LootTable';
  id?: Maybe<Scalars['String']['output']>;
  weight: Scalars['Float']['output'];
};

export type ModFilter = {
  __typename?: 'ModFilter';
  id: Scalars['String']['output'];
};

export type NameFilter = {
  __typename?: 'NameFilter';
  pattern: Scalars['String']['output'];
};

export type Named = {
  id: Scalars['String']['output'];
  mod?: Maybe<Scalars['String']['output']>;
};

export type OffsetExtra = BaseExtra & {
  __typename?: 'OffsetExtra';
  children: Array<BlockProvider>;
  probability: Scalars['Float']['output'];
  x?: Maybe<Scalars['Int']['output']>;
  y?: Maybe<Scalars['Int']['output']>;
  z?: Maybe<Scalars['Int']['output']>;
};

export type Preset = {
  provider?: Maybe<BlockProvider>;
};

export type Query = {
  __typename?: 'Query';
  config: DimensionConfig;
  configs: Array<DimensionConfig>;
  preset: Preset;
};


export type QueryConfigArgs = {
  id?: InputMaybe<Scalars['String']['input']>;
};


export type QueryPresetArgs = {
  id?: InputMaybe<Scalars['String']['input']>;
};

export type Reference = BaseBlockProvider & WeightedEntry & {
  __typename?: 'Reference';
  except?: Maybe<Array<Filter>>;
  extra?: Maybe<Scalars['Boolean']['output']>;
  extras?: Maybe<Array<Extra>>;
  icon?: Maybe<Scalars['String']['output']>;
  id: Scalars['String']['output'];
  name?: Maybe<Scalars['String']['output']>;
  provider: BlockProvider;
  weight: Scalars['Float']['output'];
};

export type SideExtra = BaseExtra & {
  __typename?: 'SideExtra';
  children: Array<BlockProvider>;
  offset?: Maybe<Scalars['Int']['output']>;
  on?: Maybe<Direction>;
  probability: Scalars['Float']['output'];
};

export type Tag = BaseBlockProvider & Named & WeightedEntry & {
  __typename?: 'Tag';
  except?: Maybe<Array<Filter>>;
  extra?: Maybe<Scalars['Boolean']['output']>;
  extras?: Maybe<Array<Extra>>;
  icon?: Maybe<Scalars['String']['output']>;
  id: Scalars['String']['output'];
  matches: Array<Block>;
  mod?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  weight: Scalars['Float']['output'];
};

export type TagFilter = Named & {
  __typename?: 'TagFilter';
  id: Scalars['String']['output'];
  mod?: Maybe<Scalars['String']['output']>;
};

export type WeightedEntry = {
  weight: Scalars['Float']['output'];
};
